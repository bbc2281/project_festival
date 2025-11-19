package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.PaymentDTO;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment" + 
                " ( company_idx, payment_date, payment_amount, payment_account, payment_throw, payment_catch, payment_status, order_id, order_name, funding_festival_idx) " + 
                " VALUES(#{company_idx}, #{payment_date}, #{payment_amount}, #{payment_account}, #{payment_throw}, #{payment_catch}, #{payment_status}, #{order_id}, #{order_name}, #{funding_festival_idx});")
    void InsertPayment(PaymentDTO paymentDTO);


    @Update("update payment set payment_status = #{payment_status} where order_id = #{order_id}")
    void modifyProcess(PaymentDTO paymentDTO);

    @Delete("delete from payment where order_id = #{order_id}")
    void deleteProcess(PaymentDTO paymentDTO);
    
    @Select("select * "+ 
    " from payment "+ 
    " where order_id = #{order_id}")
    PaymentDTO infoProcess(String order_id);

    @Select("select * from payment where order_id = #{order_id}")
    PaymentDTO selectFundingAmount(@Param("order_id") String order_id);

    @Select("select * from payment p join festival_funding f on p.funding_festival_idx = f.funding_festival_idx where p.company_idx = #{company_idx} and p.payment_status = 'success' order by p.payment_idx desc")
    List<PaymentDTO> selectFundingByCompany(@Param("company_idx") int idx);

    @Select("select count(*) from payment where company_idx = #{company_idx} and payment_status = 'success'")
    int countPaymentByCompany(@Param("company_idx") int idx);
}
