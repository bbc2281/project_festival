package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalDTO {

    private int festival_idx;
    private int festival_category_idx;
    private int region_idx;
    private String festival_name;
    private String festival_fee;
    private String festival_begin_date;
    private String festival_end_date;
    private String festival_info;
    private String festival_host;
    private String festival_img_path;
    private String festival_address;
    private String festival_link;
    private double festival_lat;
    private double festival_lot;

    private String festival_category_name;
    private String region_name;
    private Integer seg_festival_idx;
    private int festival_like;
    private int member_idx;

    private int company_idx;
    private int festival_reg_log;
}