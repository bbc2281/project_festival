package com.soldesk.festival.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.UserResponse;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.service.DataApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyRestController {

    private final DataApiService dataService;
    
    @PostMapping("/verifybusiness")
    public ResponseEntity<UserResponse> verifyBusinessNumber(@RequestParam("b_no")String businessnum, @RequestParam("p_nm")String owner){
        
        try {
             boolean isvalid = dataService.verifyBusinessNumber(businessnum, owner);
             
             if(isvalid){
                UserResponse response = UserResponse.successMessage("유효한 사업자 번호입니다.");
                  return ResponseEntity.ok(response);
             } else {
                 UserResponse response = UserResponse.error("유효하지 않은 사업자 번호입니다.");
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            	  
		
		} catch (UserException e) {
			 UserResponse response = UserResponse.error(e.getMessage());
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}catch (Exception e){
			e.printStackTrace();
			UserResponse response = UserResponse.error("회원가입 중 오류가 발생하였습니다");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
    
    }

}
