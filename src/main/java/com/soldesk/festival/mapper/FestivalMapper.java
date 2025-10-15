package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.FestivalDTO;

@Mapper
public interface FestivalMapper {
    
    @Select("select * from festival f " +
                "join festival_category c on f.festival_category_idx = c.festival_category_idx " +
                "join region r on r.region_idx  = f.region_idx ")
    List<FestivalDTO> selectAllFestival();

    @Select("select * from festival where festival_idx = #{festival_idx}")
    FestivalDTO selectFestival(@Param("festival_idx") int id);
}
