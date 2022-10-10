package am.solidoGroup.companyemployeespring.service;

import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${company.employee.management.imageFolder}")
    private String picFolder;
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public void saveUser(MultipartFile multipartFile, User user) throws IOException {
        if (user.getEmail() != null) {
            if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
                String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
                File file = new File(picFolder + File.separator + fileName);
                multipartFile.transferTo(file);
                user.setPictUrl(fileName);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }
    public byte[] getImage(String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(picFolder + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }
    public void save(CurrentUser currentUser) {
        userRepository.save(currentUser.getUser());
    }
}
