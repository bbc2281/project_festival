package com.soldesk.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private boolean success;
    private String message;
    private Object data;
    private String redirectUrl;
    
    // 성공 data + message 반환
    public static UserResponse success(String message, Object data){
        return UserResponse.builder().success(true).message(message).data(data).build();
    }
    
    // 성공 message 반환
    public static UserResponse successMessage(String message){
        return UserResponse.builder().success(true).message(message).data(null).build();
    }
    
    //Error message 반환
    public static UserResponse error(String err){
        return UserResponse.builder().success(false).message(err).data(null).build();
    }

    //로그인 요청페이지 저장
    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

}
