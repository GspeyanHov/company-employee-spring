package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${company.employee.management.imageFolder}")
    private String picFolder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String getUsers(ModelMap modelMap) {
        List<User> allUsers = userRepository.findAll();
        modelMap.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/users/add")
    public String addUserPage() {
        return "addUsers";
    }

    @PostMapping(value = "/users/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam("userImage") MultipartFile multipartFile) throws IOException {
        if (user.getEmail() == null) {
            return "redirect:/";

        } else if (user.getEmail() != null) {
            if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
                String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
                File file = new File(picFolder + File.separator + fileName);
                multipartFile.transferTo(file);
                user.setPictUrl(fileName);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
        return "redirect:/users";
    }

    @GetMapping(value = "/users/getImage",  produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getUserImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream fileInputStream = new FileInputStream(picFolder + File.separator + fileName);
        return IOUtils.toByteArray(fileInputStream);
    }
}



