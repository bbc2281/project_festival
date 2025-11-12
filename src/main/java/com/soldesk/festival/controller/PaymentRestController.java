package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soldesk.festival.config.TossConfig;
import com.soldesk.festival.dto.PaymentRequestDTO;
import com.soldesk.festival.dto.companyDTO;
import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.service.paymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());//클래스 이름으로 구분해서 로그 찍는 변수.
    private final TossConfig tossConfig;
    private final paymentService paymentService;
    
    
    @PostMapping("/order")
    //주문 요청 만들기 
    public ResponseEntity<Map<String,Object>> createOrder(@RequestBody PaymentRequestDTO requestDTO , @SessionAttribute("companyMember")companyDTO companyDTO){
        companyDTO.setCompany_idx(1); // 로그인 기능이 연동이 되어 있지 않아 설정한 임시 idx입니다 로그인 기능 연동 후 삭제 해야합니다.

        Map<String,Object> response = new HashMap<String,Object>();
        try {   
            logger.info(" 주문 생성 요청 들어옴: {}", requestDTO); 
            String orderId = paymentService.saveOrderAndReturnOrderId(requestDTO,companyDTO);
            response.put("message", "주문생성완료");
            response.put("orderId", orderId);
            response.put("amount", requestDTO.getAmount());
            response.put("orderName", requestDTO.getOrderName());
            logger.info("저장되는 금액: {}", requestDTO.getAmount());
            logger.info("주문이름: {}", requestDTO.getOrderName());
            logger.info("toss register 요청 orderId={} amount={}", orderId, requestDTO.getAmount());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error(" 주문 생성 중 오류 발생", e); 
            response.put("message", "주문생성실패");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/confirm")   
    public ResponseEntity<Map<String,Object>> confirmPayment(@RequestBody Map<String,String> body){
        logger.info("Loaded Toss Secret Key: {}", tossConfig.getSecretKey());
        try {
            logger.info("Toss Secret Key: {}", tossConfig.getSecretKey());
            String paymentKey = body.get("paymentKey");
            String orderId = body.get("orderId");
            String amount = body.get("amount");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(tossConfig.getSecretKey(),"");
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> request = new HashMap<>();
            request.put("paymentKey", paymentKey);
            request.put("orderId", orderId);
            request.put("amount", amount);

            HttpEntity<Map<String,String>> entity = new HttpEntity<>(request,headers); 

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                entity, 
                String.class);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response.getBody(), Map.class);
            logger.info("Toss Secret Key: {}", tossConfig.getSecretKey());
            return ResponseEntity.status(response.getStatusCode()).body(json);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", "INTERNAL_ERROR");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);  
        }
    }

    @PostMapping("/success")
    public ResponseEntity<Map<String,Object>> sucessPayment(@RequestBody Map<String,Object> request){
        Map<String,Object> response = new HashMap<String,Object>();
        PaymentDTO paymentDTO = new PaymentDTO();
        System.out.println("request map: " + request);
        try {
            paymentDTO.setOrder_id(request.get("orderId").toString());
            paymentService.modifyProcess(paymentDTO);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fail")
    public ResponseEntity<Map<String,Object>> failPayment(@RequestBody PaymentRequestDTO requestDTO){
        Map<String,Object> response = new HashMap<String,Object>();
        PaymentDTO paymentDTO = new PaymentDTO();
        try {
            paymentDTO.setOrder_id(requestDTO.getOrderId());
            paymentService.deleteProcess(paymentDTO);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/info") //주문 정보를 가져오는 메서드입니다 
    public ResponseEntity<Map<String,Object>> infoPayment(@RequestBody Map<String,Object> request, @SessionAttribute("companyMember")companyDTO companyDTO){
        Map<String,Object> response = new HashMap<String,Object>();
        String orderId = request.get("orderId").toString();
        try {
            PaymentDTO infoPaymentDTO =paymentService.infoProcess(orderId);
            response.put("orderId", infoPaymentDTO.getOrder_id()) ;
            response.put("amount",infoPaymentDTO.getPayment_amount());
            response.put("orderName", infoPaymentDTO.getOrder_name());

            //구매 고객의 이메일 과 id를 보내야 합니다 
            response.put("customerKey", companyDTO.getMember_id());
            response.put("customerEmail", companyDTO.getMember_email());
            response.put("customerName", companyDTO.getCompany_name());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
}
