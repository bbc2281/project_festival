package com.soldesk.festival.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundingFestivalDTO {
    
    int funding_festival_idx;
    int festival_category_idx;
    int company_idx;
    String company_account;
    String festival_name;
    String festival_fee;
    String festival_begin_date;
    String festival_end_date;
    String festival_info;
    String festival_host;
    String festival_img_path;
    String festival_address;
    String festival_file;
    int festival_total_amount;
    int festival_amount;
    String festival_short;

    String funding_end_date;
    String festival_category_name;
}
