    package com.soldesk.festival.controller;

    import java.util.HashMap;
    import java.util.Map;

    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        
        @PostMapping(value = "/modify",
             consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE)
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

        @PostMapping("/delete-image")
        public ResponseEntity<?> deleteModifyImage(@RequestParam("review_idx")int review_idx){
            ReviewDTO modifyImageReview = reviewService.infoProcess(review_idx);
            if (modifyImageReview == null) {
                return ResponseEntity.ok(Map.of("success", false, "message", "리뷰를 찾을 수 없습니다"));
            }

            String existingImage = modifyImageReview.getReview_img_path();
            if (existingImage == null) {
                return ResponseEntity.ok(Map.of("success", false, "message", "이미지가 없습니다"));
            }
            try {
                String firebaeUrl = fileUploadService.extractPathFromUrl(existingImage);
                fileUploadService.deleteFromFirebase(firebaeUrl);
                modifyImageReview.setReview_img_path(null);
                reviewService.modifyProcess(modifyImageReview);
                return ResponseEntity.ok(Map.of("success",true));  
            } catch (Exception e) {
                return ResponseEntity.ok(Map.of("success",false,"message",e.getMessage()));
            }
        }


        @PostMapping("/delete")
        public ResponseEntity<Map<String,Object>> deleteReview(@RequestBody Map<String, Integer> payload) {
            Map<String,Object> response = new HashMap<>();
            int review_idx = payload.get("review_idx");
            try {
                ReviewDTO deleteReview = reviewService.infoProcess(review_idx);
                if (deleteReview == null) {
                    response.put("success", false);
                    response.put("message", "리뷰를 찾을 수 없습니다.");
                    return ResponseEntity.ok(response);
                }

                String reviewImg = deleteReview.getReview_img_path();
                if (reviewImg != null && !reviewImg.isEmpty()) {
                    String firebaseUrl = fileUploadService.extractPathFromUrl(reviewImg);
                    fileUploadService.deleteFromFirebase(firebaseUrl);
                }
                reviewService.deleteReview(review_idx);
                response.put("success", true);
                response.put("message", "리뷰가 삭제되었습니다.");
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "삭제 중 오류 발생: " + e.getMessage());
            }
            return ResponseEntity.ok(response);
        }

    }
