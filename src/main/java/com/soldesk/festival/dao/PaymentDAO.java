package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentDAO {
    
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

    public PaymentDTO selectFundingAmount(String orderId){
        return paymentMapper.selectFundingAmount(orderId);
    }
    
    public List<PaymentDTO> selectFundingByCompany(int idx){
        return paymentMapper.selectFundingByCompany(idx);
    }

    public int countPaymentByCompany(int idx){
        return paymentMapper.countPaymentByCompany(idx);
    }
}
