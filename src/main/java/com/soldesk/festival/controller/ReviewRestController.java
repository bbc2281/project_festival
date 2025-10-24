    package com.soldesk.festival.controller;

    import java.util.HashMap;
    import java.util.Map;

    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.bind.annotation.SessionAttribute;
    import org.springframework.web.multipart.MultipartFile;

    import com.soldesk.festival.dto.MemberDTO;
    import com.soldesk.festival.dto.ReviewDTO;
    import com.soldesk.festival.service.FileUploadService;
    import com.soldesk.festival.service.ReviewService;

    import lombok.RequiredArgsConstructor;

    @RestController
    @RequestMapping("/review")
    @RequiredArgsConstructor
    public class ReviewRestController {

        private final ReviewService reviewService;
        private final FileUploadService fileUploadService;

        @PostMapping(value = "/write" ,
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Map<String,Object>> writeSubmit(@ModelAttribute ReviewDTO reviewDTO,
                @RequestParam(value = "upload_file" , required=false)MultipartFile file, 
                @SessionAttribute(value = "loginMember" , required = false)MemberDTO memberDTO){

            Map<String,Object> response = new HashMap<String,Object>();
            try{
                if(file != null && !file.isEmpty()){
                    String imgUrl = fileUploadService.uploadToFirebase(file);
                    reviewDTO.setReview_img_path(imgUrl);
                }
                reviewService.writeProcess(reviewDTO,memberDTO);
                response.put("success", true);
            }catch(Exception e){
                response.put("success", false);
                response.put("message", e.getMessage());
            }
            return ResponseEntity.ok(response);
        }  
        
        public ResponseEntity<Map<String,Object>> modifySubmit(@ModelAttribute ReviewDTO reviewDTO, 
        @RequestParam(value = "upload_file" , required = false)MultipartFile file){
            Map<String,Object> response = new HashMap<String,Object>();
            ReviewDTO modifyReview = reviewService.infoProcess(reviewDTO.getReview_idx());
            if (modifyReview == null) {
                response.put("success", false);
                response.put("message", "리뷰 정보를 찾을 수 없습니다.");
                 return ResponseEntity.ok(response);
                }
            try {
                if(file != null && !file.isEmpty()){
                    if(modifyReview.getReview_img_path() != null && !modifyReview.getReview_img_path().isEmpty()){
                        String imgPath = fileUploadService.extractPathFromUrl(modifyReview.getReview_img_path());
                        fileUploadService.deleteFromFirebase(imgPath);
                    }
                    String uploadImage = fileUploadService.uploadToFirebase(file);
                    reviewDTO.setReview_img_path(uploadImage);
                }else{
                    reviewDTO.setReview_img_path(modifyReview.getReview_img_path());
                }
                reviewService.modifyProcess(reviewDTO);
                response.put("success", true);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", e.getMessage());
            }
            return ResponseEntity.ok(response);
        }
        
    }
