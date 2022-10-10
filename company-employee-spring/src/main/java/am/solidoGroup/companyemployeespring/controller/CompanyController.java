package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import am.solidoGroup.companyemployeespring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    @GetMapping("/companies")
    private String allCompanies(ModelMap modelMap){
        List<Company> companies = companyService.findAll();
        modelMap.addAttribute("companies",companies);
        return "companies";
    }
    @GetMapping("/companies/add")
    private String addCompaniesPage(){
        return "addCompanies";
    }
    @PostMapping("/companies/add")
    public String addCompany(@ModelAttribute Company company,@AuthenticationPrincipal CurrentUser currentUser){
        companyService.save(company,currentUser);
        return "redirect:/companies";
    }
}
