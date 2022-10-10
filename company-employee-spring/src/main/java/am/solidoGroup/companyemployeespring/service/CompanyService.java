package am.solidoGroup.companyemployeespring.service;

import am.solidoGroup.companyemployeespring.entity.Company;
import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.CompanyRepository;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public void save(Company company, @AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        company.setUser(user);
        userRepository.save(user);
        companyRepository.save(company);
    }
}
