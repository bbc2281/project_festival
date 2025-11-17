package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.fundingFestivalDTO;
import com.soldesk.festival.service.FundingService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FundingFestivalDTO;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.FileUploadService;
import com.soldesk.festival.service.FundingFestivalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/funding")
public class FundingController {
    
    private final FestivalService festivalService;
    private final FundingFestivalService fundingFestivalService;
    private final FileUploadService fileUploadService;

    @GetMapping("/main")
    public String main(Model model){

        List<FestivalCategoryDTO> category = festivalService.getCategory();
        model.addAttribute("category", category);

        List<FundingFestivalDTO> fundingList = fundingFestivalService.selectAllFunding();
        model.addAttribute("fundingList", fundingList);

        return "funding/main";
    }

    @GetMapping("/info")
    public String info(@RequestParam("id") int idx, Model model, HttpSession session){

         
        //해당 객체를 모델에 넣어 보내야만 결제 시스템이 정상 작동 됩니다.
        model.addAttribute("fundingFestival", fundingFestivalDTO);

        //임시용 으로 세션에 객체를 넣었습니다.
        CompanyDTO companyDTO = new CompanyDTO();
        session.setAttribute("companyMember", companyDTO);
      
        FundingFestivalDTO funding = fundingFestivalService.selectFunding(idx);
        model.addAttribute("funding", funding);

        return "funding/info";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("funding") FundingFestivalDTO funding, Model model){

        List<FestivalCategoryDTO> category = festivalService.getCategory();
        model.addAttribute("category", category);

        return "funding/register";
    }

    @PostMapping("/register")
    public String regFunding(@ModelAttribute("funding") FundingFestivalDTO fundingDTO, @RequestParam(value = "img_path", required = false) MultipartFile imgFile,  @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session){

        CompanyDTO login = (CompanyDTO) session.getAttribute("companyMember");

        String uploadDir = "C:\\soldesk\\project_festival\\festival\\festival\\project_festival\\src\\main\\resources\\static\\uploads";
        
        try {
            if(!imgFile.isEmpty()){
                String imageUrl;
                try {
                System.out.println("파일 이름: " + imgFile.getOriginalFilename());
                System.out.println("파일 크기: " + imgFile.getSize());
                imageUrl = fileUploadService.uploadToFirebase(imgFile);
                fundingDTO.setFestival_img_path(imageUrl);
            
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path savePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath.toFile());

                fundingDTO.setFestival_file("/uploads/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        };
        
        fundingFestivalService.insertFunding(fundingDTO, login);

        return "redirect:/funding/main";
    }
}
