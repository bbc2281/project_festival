package com.soldesk.festival.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.FavoriteDTO;

@Mapper
public interface FavoriteMapper {
    
    @Select("select count(*) from favorite where member_idx = #{member_idx} and festival_idx = #{festival_idx}")
    int existsFavorite(FavoriteDTO favorite);

    @Insert("insert into favorite (member_idx, festival_idx) values (#{member_idx}, #{festival_idx})")
    void insertFavorite(FavoriteDTO favorite);

    @Delete("delete from favorite where member_idx = #{member_idx} and festival_idx = #{festival_idx}")
    void deleteFavorite(FavoriteDTO favorite);

    @Select("select count(favorite_idx) from favorite where festival_idx = #{festival_idx}")
    int countFavoriteByFestival(@Param("festival_idx") int festival_idx);
}
