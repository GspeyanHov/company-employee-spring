package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.EmployeeRepository;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class CompanyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    private String allCompanies(ModelMap modelMap){
        List<Company> companies = companyRepository.findAll();
        modelMap.addAttribute("companies",companies);
        return "companies";
    }
    @GetMapping("/companies/add")
    private String addCompaniesPage(){
        return "addCompanies";
    }
    @PostMapping("/companies/add")
    public String addCompany(@ModelAttribute Company company,@AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        company.setUser(user);
        userRepository.save(user);
        companyRepository.save(company);
        return "redirect:/companies";
    }
}
