package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.EmployeeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class EmployeeController {

    @Value("${company.employee.management.imageFolder}")
    private String picFolder;
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
    public String addEmployee(@ModelAttribute Employee employee,
                          @RequestParam("userImage") MultipartFile file) throws IOException {
        if(!file.isEmpty() && file.getSize() > 0){
            String fileName =  System.nanoTime() + "_" + file.getOriginalFilename();
            File newFile = new File(picFolder + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }
    @GetMapping(name = "/employees/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(picFolder + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }
    @GetMapping("/employees/delete")
    public String deleteEmployee(@RequestParam("id") int id){
        employeeRepository.deleteById(id);
        return "redirect:/employees";
    }
}
