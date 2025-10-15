package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalDTO {

    int festival_idx;
    int festival_category_idx;
    int region_idx;
    String festival_name;
    String festival_fee;
    String festival_begin_date;
    String festival_end_date;
    String festival_info;
    String festival_host;
    String festival_img_path;
    String festival_address;
    String festival_link;

}