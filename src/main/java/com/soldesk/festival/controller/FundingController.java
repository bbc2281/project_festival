package com.soldesk.festival.controller;

import java.io.IOException;
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
    public String info(@RequestParam("id") int idx, Model model){

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
    public String regFunding(@ModelAttribute("funding") FundingFestivalDTO fundingDTO, @RequestParam("img_path") MultipartFile imgFile, HttpSession session){

        CompanyDTO login = (CompanyDTO) session.getAttribute("companyMember");

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

        fundingFestivalService.insertFunding(fundingDTO, login);

        return "redirect:/funding/main";
    }
}
