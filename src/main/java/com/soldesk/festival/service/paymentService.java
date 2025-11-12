package com.soldesk.festival.service;

import java.sql.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.paymentDAO;
import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.dto.PaymentRequestDTO;
import com.soldesk.festival.dto.companyDTO;
import com.soldesk.festival.dto.fundingFestivalDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class paymentService {

    private final paymentDAO paymentDAO;
    private final FundingService fundingService;

    public String saveOrderAndReturnOrderId(PaymentRequestDTO requestDTO , companyDTO companyDTO){
        PaymentDTO paymentDTO = new PaymentDTO();
        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        paymentDTO.setOrder_id(orderId);
        fundingFestivalDTO fundingFestivalDTO = fundingService.selectFundingFestival(requestDTO.getFestivalIdx());
        paymentDTO.setCompany_idx(fundingFestivalDTO.getCompany_idx());
        paymentDTO.setPayment_account(fundingFestivalDTO.getCompany_account());
        paymentDTO.setPayment_amount(requestDTO.getAmount());
        paymentDTO.setPayment_status("PENDING");
        paymentDTO.setPayment_throw(companyDTO.getCompany_name());
        paymentDTO.setOrder_name(requestDTO.getOrderName());
        // 결국 개최자가 돈을 받는건 똑같기 때문에 연동 하는데 어려움이 있어 catch 칼럼은 삭제를 고려중입니다 .
        paymentDTO.setPayment_catch("임시용 123");
        paymentDTO.setPayment_date(new Date(System.currentTimeMillis()));
        paymentDAO.InsertPayment(paymentDTO);
        return paymentDTO.getOrder_id();
    }


    public void modifyProcess(PaymentDTO paymentDTO){
        paymentDTO.setPayment_status("success");
        System.out.println(paymentDTO.getOrder_id());
        paymentDAO.modifyProcess(paymentDTO);
    }

    public void deleteProcess(PaymentDTO paymentDTO){
        paymentDAO.deleteProcess(paymentDTO);
    }

    public PaymentDTO infoProcess(String orderId){

        return paymentDAO.infoProcess(orderId);
    }
    
    
}
