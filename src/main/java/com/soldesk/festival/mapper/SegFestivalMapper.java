package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.FestivalDTO;

@Mapper
public interface SegFestivalMapper {
    
    @Insert("insert into seg_festival(company_idx, festival_category_idx, region_idx, festival_name, festival_fee, festival_begin_date, festival_end_date, festival_info, festival_host, festival_img_path, festival_address, festival_link, festival_lat, festival_lot) values (#{company_idx} ,#{festival_category_idx}, #{region_idx}, #{festival_name}, #{festival_fee}, #{festival_begin_date}, #{festival_end_date}, #{festival_info}, #{festival_host}, #{festival_img_path}, #{festival_address}, #{festival_link}, #{festival_lat}, #{festival_lot})")
    void insertSegFestival(FestivalDTO festivalDTO);

    @Select("select * from seg_festival where seg_festival_idx = #{seg_festival_idx}")
    FestivalDTO selectFestival(@Param("seg_festival_idx") int seg_festival_idx);

    @Select("select * from seg_festival where company_idx = #{company_idx}")
    List<FestivalDTO> selectFestivalByCompany(@Param("company_idx") int company_idx);

    @Select("select * from seg_festival")
    List<FestivalDTO> selectAllFestivals();

    @Delete("delete from seg_festival where seg_festival_idx = #{seg_festival_idx}")
    void deleteFestival(@Param("seg_festival_idx") int seg_festival_idx);

    @Update("update seg_festival set festival_reg_log = 1 where seg_festival_idx = #{seg_festival_idx}")
    void updateSetLog(@Param("seg_festival_idx") int seg_festival_id);

    @Update("update seg_festival set festival_reg_log = 0 where seg_festival_idx = #{seg_festival_idx}")
    void updateDelLog(@Param("seg_festival_idx") int seg_festival_id);
}
