package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.RegionDTO;

@Mapper
public interface FestivalMapper {
    
    @Select("select * from festival f join festival_category c on f.festival_category_idx = c.festival_category_idx join region r on r.region_idx  = f.region_idx left join (select festival_idx , count(favorite_idx) as festival_like from favorite fa group by festival_idx) fa on f.festival_idx = fa.festival_idx")
    List<FestivalDTO> selectAllFestival();

    @Select("select * from festival where festival_idx = #{festival_idx}")
    FestivalDTO selectFestival(@Param("festival_idx") int id);

    @Select("select * from festival_category")
    List<FestivalCategoryDTO> selectAllCategory();

    @Select("select * from region order by region_idx asc")
    List<RegionDTO> selectAllRegion();

    @Insert("insert into festival (festival_category_idx, region_idx, festival_name, festival_fee, festival_begin_date, festival_end_date, festival_info, festival_host, festival_img_path, festival_address, festival_link, festival_lat, festival_lot) values (#{festival_category_idx}, #{region_idx}, #{festival_name}, #{festival_fee}, #{festival_begin_date}, #{festival_end_date}, #{festival_info}, #{festival_host}, #{festival_img_path}, #{festival_address}, #{festival_link}, #{festival_lat}, #{festival_lot})")
    void insertFestival(FestivalDTO festival);

    @Delete("delete from festival where festival_idx = #{festival_idx}")
    void deleteFestival(@Param("festival_idx") int festival_idx);

    @Select("select count(festival_idx) from festival")
    int countFestival();
}
