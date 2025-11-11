package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.FavoriteDTO;
import com.soldesk.festival.dto.FestivalDTO;

@Mapper
public interface FavoriteMapper {
    
    @Select("select count(*) from favorite where member_idx = #{member_idx} and festival_idx = #{festival_idx}")
    int existsFavoriteByMember(@Param("member_idx") int member_idx, @Param("festival_idx") int festival_idx);

    @Select("select count(*) from favorite where company_idx = #{company_idx} and festival_idx = #{festival_idx}")
    int existsFavoriteByCompany(@Param("company_idx") int company_idx, @Param("festival_idx") int festival_idx);

    @Insert("insert into favorite (member_idx, festival_idx) values (#{member_idx}, #{festival_idx})")
    void insertFavoriteByMember(FavoriteDTO favorite);
    @Insert("insert into favorite (festival_idx, company_idx) values (#{festival_idx}, #{company_idx})")
    void insertFavoriteByCompany(FavoriteDTO favorite);

    @Delete("delete from favorite where member_idx = #{member_idx} and festival_idx = #{festival_idx}")
    void deleteFavoriteByMember(FavoriteDTO favorite);

    @Delete("delete from favorite where company_idx = #{company_idx} and festival_idx = #{festival_idx}")
    void deleteFavoriteByCompany(FavoriteDTO favorite);

    @Select("select count(favorite_idx) from favorite where festival_idx = #{festival_idx}")
    int countFavoriteByFestival(@Param("festival_idx") int festival_idx);

    @Select("select * from festival f join favorite fa on f.festival_idx = fa.festival_idx where fa.member_idx = #{member_idx}")
    List<FestivalDTO> selectAllFavoriteByUser(@Param("member_idx") int member_idx);

    @Select("select * from festival f join favorite fa on f.festival_idx = fa.festival_idx where fa.company_idx = #{company_idx}")
    List<FestivalDTO> selectAllFavoriteByCompany(@Param("company_idx") int company_idx);
}
