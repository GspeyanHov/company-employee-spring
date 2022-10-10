package am.solidoGroup.companyemployeespring.service;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.Employee;
import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.EmployeeRepository;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    @Value("${company.employee.management.imageFolder}")
    private String picFolder;

    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public void save(Employee employee, MultipartFile file, CurrentUser currentUser) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.nanoTime() + "_" + file.getOriginalFilename();
            File newFile = new File(picFolder + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);
        }
        Company company = employee.getCompany();
        company.setSize(company.getSize() + 1);
        User user = currentUser.getUser();
        employee.setUser(user);
        companyRepository.save(company);
        userRepository.save(user);
        employeeRepository.save(employee);
    }

    public byte[] getImage(String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(picFolder + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }

    public void findById(int id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            optionalEmployee.get().getCompany().setSize(optionalEmployee.get().getCompany().getSize() - 1);
            companyRepository.save(optionalEmployee.get().getCompany());
        }
    }

    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }
}
