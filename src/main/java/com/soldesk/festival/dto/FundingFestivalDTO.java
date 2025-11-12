package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundingFestivalDTO {
    
    private int funding_festival_idx;
    private int festival_category_idx;
    private int company_idx;
    private String company_account;
    private String festival_name;
    private String festival_fee;
    private String festival_begin_date;
    private String festival_end_date;
    private String festival_info;
    private String festival_host;
    private String festival_img_path;
    private String festival_address;
    private String festival_file;
    private int festival_total_amount;
    private int festival_amount;
    private String festival_short;
    private String funding_end_date;

    private String festival_category_name;
}
