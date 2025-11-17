package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.FundingFestivalDTO;

@Mapper
public interface FundingFestivalMapper {
    
    @Select("select * from festival_funding f join festival_category c on f.festival_category_idx = c.festival_category_idx")
    List<FundingFestivalDTO> selectAllFunding();

    @Select("select * from festival_funding f join festival_category c on f.festival_category_idx = c.festival_category_idx where funding_festival_idx = #{funding_festival_idx}")
    FundingFestivalDTO selectFunding(@Param("funding_festival_idx") int idx);

    @Insert("insert into festival_funding (festival_category_idx, company_idx, company_account, festival_name, festival_fee, festival_begin_date, festival_end_date, festival_info, festival_host, festival_img_path, festival_address, festival_file, festival_total_amount, festival_short, funding_end_date, festival_amount) values (#{festival_category_idx}, #{company_idx}, #{company_account}, #{festival_name}, #{festival_fee}, #{festival_begin_date}, #{festival_end_date}, #{festival_info}, #{festival_host}, #{festival_img_path}, #{festival_address}, #{festival_file}, #{festival_total_amount}, #{festival_short}, #{funding_end_date}, 0)")
    void insertFunding(FundingFestivalDTO funding);

    @Select("select count(*) from festival_funding where company_idx = #{company_idx}")
    int countFundingByCompany(@Param("company_idx") int idx);

    @Update("")
    void insertFundingAmount(@Param("festival_amount") int festival_amount);
}
