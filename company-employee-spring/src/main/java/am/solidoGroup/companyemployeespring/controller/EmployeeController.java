package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.EmployeeRepository;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Value("${company.employee.management.imageFolder}")
    private String picFolder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public String allEmployees(ModelMap modelMap){
        List<Employee> employees = employeeRepository.findAll();
        modelMap.addAttribute("employees",employees);
        return "employees";
    }
    @GetMapping("/employees/add")
    public String addEmployeePage(ModelMap modelMap){
        List<Company> all = companyRepository.findAll();
        modelMap.addAttribute("allCompanies",all);
        return "addEmployees";
    }
    @PostMapping("/employees/add")
    public String addEmployees(@ModelAttribute Employee employee,
                               @RequestParam("userImage") MultipartFile file,
                               @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        if(!file.isEmpty() && file.getSize() > 0){
            String fileName =  System.nanoTime() + "_" + file.getOriginalFilename();
            File newFile = new File(picFolder + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);
        }
        Company company = employee.getCompany();
        company.setSize(company.getSize() + 1);
        User user = currentUser.getUser();
        employee.setUser(user);
        userRepository.save(user);
        employeeRepository.save(employee);
        companyRepository.save(company);
        return "redirect:/employees";
    }
    @GetMapping(value = "/employees/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(picFolder + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }
    @GetMapping("/employees/delete")
    public String deleteEmployee(@RequestParam("id") int id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            optionalEmployee.get().getCompany().setSize(optionalEmployee.get().getCompany().getSize() - 1);
           companyRepository.save(optionalEmployee.get().getCompany());
        }
        employeeRepository.deleteById(id);
        return "redirect:/employees";

    }
}
