package com.soldesk.festival.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soldesk.festival.config.CompanyRole;

@MappedTypes(CompanyRole.class)
@Configuration
@MapperScan(basePackages="com.soldesk.festival.mapper")
public class CompanyRoleTypeHandler extends BaseTypeHandler<CompanyRole>{
    
    @Bean
    public CompanyRoleTypeHandler companyRoleHandler(){
        return new CompanyRoleTypeHandler();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CompanyRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public CompanyRole getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rolename = rs.getString(columnName);
        return CompanyRole.fromString(rolename);
    }

    @Override
    public CompanyRole getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rolename = rs.getString(columnIndex);
        return CompanyRole.fromString(rolename);
    }

    @Override
    public CompanyRole getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rolename = cs.getString(columnIndex);
        return CompanyRole.fromString(rolename);
    }

}
