package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import am.solidoGroup.companyemployeespring.service.CompanyService;
import am.solidoGroup.companyemployeespring.service.EmployeeService;
import am.solidoGroup.companyemployeespring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CompanyService companyService;
    private final UserService userService;

    @GetMapping("/employees")
    public String allEmployees(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               ModelMap modelMap) {
        int currentPage = page.orElse(1);
        int currentSize = size.orElse(5);
        Page<Employee> employees = employeeService.findAll(PageRequest.of(currentPage - 1,currentSize));
        modelMap.addAttribute("employees", employees);
        int totalPages = employees.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers",pageNumbers);
        }
        return "employees";
    }

    @GetMapping("/employees/add")
    public String addEmployeePage(ModelMap modelMap) {
        List<Company> all = companyService.findAll();
        modelMap.addAttribute("allCompanies", all);
        return "addEmployees";
    }

    @PostMapping("/employees/add")
    public String addEmployees(@ModelAttribute Employee employee,
                               @RequestParam("userImage") MultipartFile file,
                               @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        userService.save(currentUser);
        employeeService.save(employee,file,currentUser);
        return "redirect:/employees";
    }

    @GetMapping(value = "/employees/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        return employeeService.getImage(fileName);
    }

    @GetMapping("/employees/delete")
    public String deleteEmployee(@RequestParam("id") int id) {
        employeeService.findById(id);
        employeeService.deleteById(id);
        return "redirect:/employees";

    }
}
