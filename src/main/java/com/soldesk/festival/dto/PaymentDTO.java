package com.soldesk.festival.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    
    private int payment_idx;
    private int company_idx;
    private Date payment_date;
    private int payment_amount; // 결제 금액
    private String payment_account; // 돈 들어갈 계좌
    private String payment_throw; //돈 준 사람
    private String payment_catch; //돈 받은 사람
    private String payment_status; // 현재 결제 진행 상태
    private String order_id; //주문 번호 
    private String order_name; //주문 이름 



}
