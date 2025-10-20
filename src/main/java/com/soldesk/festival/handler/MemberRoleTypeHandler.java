package com.soldesk.festival.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.soldesk.festival.config.MemberRole;

@MappedTypes(MemberRole.class)
public class MemberRoleTypeHandler extends BaseTypeHandler<MemberRole> {
    //1.java객체(enum)을 db에 저장할때 : enum ->  string
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MemberRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }
    
    // 2. DB에서 String값을 읽어와 자바 객체(enum)으로 변환할때 (string -> enum)
    @Override
    public MemberRole getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rolename = rs.getString(columnName);
        return MemberRole.fromString(rolename);
    }

    @Override
    public MemberRole getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rolename = rs.getString(columnIndex);
        /* 
        if(rolename == null) {
            throw new SQ
        }
        */

        return MemberRole.fromString(rolename);
    }

    @Override
    public MemberRole getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rolename = cs.getString(columnIndex);
        return MemberRole.fromString(rolename);
    }

}
