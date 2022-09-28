package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/companies")
    private String allCompanies(ModelMap modelMap){
        List<Company> companies = companyRepository.findAll();
        modelMap.addAttribute("companies",companies);
        return "companies";
    }
    @GetMapping("/companies/add")
    private String addCompaniesPage(ModelMap modelMap){
        List<Company> companyList = companyRepository.findAll();
        modelMap.addAttribute("companyList",companyList);
        return "addCompanies";
    }
    @PostMapping("/companies/add")
    public String addCompany(@ModelAttribute Company company){
        List<Employee> employ = employeeRepository.findAll();
        if(company.getSize() > 0 && employ.size() > 0){
            company.setSize(employ.size());
        }
        companyRepository.save(company);
        return "redirect:/companies";
    }
}
