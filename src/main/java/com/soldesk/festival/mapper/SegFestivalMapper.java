package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.FestivalDTO;

@Mapper
public interface SegFestivalMapper {
    
    @Insert("insert into seg_festival(festival_category_idx, region_idx, festival_name, festival_fee, festival_begin_date, festival_end_date, festival_info, festival_host, festival_img_path, festival_address, festival_link, festival_lat, festival_lot) values (#{festival_category_idx}, #{region_idx}, #{festival_name}, #{festival_fee}, #{festival_begin_date}, #{festival_end_date}, #{festival_info}, #{festival_host}, #{festival_img_path}, #{festival_address}, #{festival_link}, #{festival_lat}, #{festival_lot})")
    void insertSegFestival(FestivalDTO festivalDTO);

    @Select("select * from seg_festival where seg_festival_idx = #{seg_festival_idx}")
    FestivalDTO selectFestival(@Param("seg_festival_idx") int seg_festival_idx);

    @Select("select * from seg_festival")
    List<FestivalDTO> selectAllFestivals();

    @Delete("delete from seg_festival where seg_festival_idx = #{seg_festival_idx}")
    void deleteFestival(@Param("seg_festival_idx") int seg_festival_idx);
}
