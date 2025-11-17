package com.soldesk.festival.dao;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class paymentDAO {
    
    private final PaymentMapper paymentMapper;

    public void InsertPayment(PaymentDTO paymentDTO){
        paymentMapper.InsertPayment(paymentDTO);
    }

    public void modifyProcess(PaymentDTO paymentDTO){
        paymentMapper.modifyProcess(paymentDTO);
    }

    public void deleteProcess(PaymentDTO paymentDTO){
        paymentMapper.deleteProcess(paymentDTO);
    }

    public PaymentDTO infoProcess(String orderId){

        return paymentMapper.infoProcess(orderId);
    }
    
}
