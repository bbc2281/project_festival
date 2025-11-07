package com.soldesk.festival.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DataApiService {
   
    @Value("${api.gonggong.key}")
    private String serviceKey;
    private final String Base_apiUrl = "http://api.odcloud.kr/api/nts-businessman/v1/validate";
    
    public boolean verifyBusinessNumber(String businessNumber, String representativeName) {
        
        // try 블록이 메서드 전체를 감싸도록 시작합니다.
        try {
            // 1. URL 구성 (Base_apiUrl과 serviceKey를 포함)
            String url = UriComponentsBuilder.fromHttpUrl(Base_apiUrl)
                    .queryParam("serviceKey", serviceKey)
                    .toUriString();

            // 2. 요청 JSON 본문 구성 (b_no, p_nm 포함)
            List<Map<String, String>> requestList = new ArrayList<>();
            Map<String, String> item = new HashMap<>();
            
            item.put("b_no", businessNumber);
            item.put("p_nm", representativeName); 
            
            requestList.add(item);

            // 최종 JSON Body: { "businesses": [ { "b_no": "...", "p_nm": "..." } ] }
            Map<String, Object> body = new HashMap<>();
            body.put("businesses", requestList); 

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // 3. 요청 실행
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            // 4. 결과 해석 및 안전 체크 (Null Safe 코드)
            if (response.getStatusCode() == HttpStatus.OK) {
                
                Map<String, Object> responseBody = response.getBody();
                
                if (responseBody != null) {
                    // "data" 키 존재 여부 및 List 타입 체크
                    Object dataObject = responseBody.get("data");
                    
                    if (dataObject instanceof List) {
                        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataObject;
                        
                        if (dataList != null && !dataList.isEmpty()) {
                            // b_stt_cd (사업자 상태 코드) 추출
                            String statusCode = (String) dataList.get(0).get("b_stt_cd");
                            return "01".equals(statusCode); // 01 = 정상사업자
                        }
                    }
                }
            }
        // 5. 예외 처리
        } catch (Exception e) {
            System.err.println("사업자 등록 상태 조회 중 API 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        // API 호출 실패, 응답 오류, 또는 정상 사업자가 아닌 경우
        return false; 
    }
    
}
