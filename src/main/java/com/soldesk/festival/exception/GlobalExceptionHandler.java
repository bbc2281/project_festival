package com.soldesk.festival.exception;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.soldesk.festival.dto.UserResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        Map<String, Object> errorMap = new HashMap<>();
        
        // 필드별 오류 메시지를 Map에 담습니다.
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        
        // 최종적으로 클라이언트가 이해할 수 있는 JSON 형태로 응답합니다.
        // 상태 코드 400 (Bad Request)을 반환해야 합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "유효성 검사 실패");
        response.put("errors", errorMap);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 2. 인증 실패 처리 (새로 추가)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        // 클라이언트에게 보낼 메시지
        response.put("message", "아이디 또는 비밀번호가 일치하지 않습니다."); 
        // 상세 에러 메시지가 필요하면 추가
        // response.put("detail", ex.getMessage());

        // UNAUTHORIZED (401) 상태 코드로 응답합니다.
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex){
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage());
            
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponse> handleAll(Exception e){
        e.printStackTrace();
        UserResponse response = UserResponse.error(" 서버오류" +  e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }

}
