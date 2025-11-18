package com.soldesk.festival.mapper;

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
}
