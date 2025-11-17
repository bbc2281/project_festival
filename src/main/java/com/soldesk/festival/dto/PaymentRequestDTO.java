    package com.soldesk.festival.dto;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class PaymentRequestDTO {
        
        
        //주문고유번호 주문이름  결제금액 결제하는 사람 이메일 순입니다. 
        private String orderId;
        //orderId는 토스 api의 필수 파라미터 라고 합니다.
        private String orderName;
        private int amount;
        private String customerEmail;
        private int companyIdx;
        private int festivalIdx;

    }
