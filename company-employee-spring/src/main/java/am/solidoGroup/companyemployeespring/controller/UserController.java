package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/user")
    public String userPage(){
        return "/user";
    }
    @GetMapping("/users")
    public String getUsers(ModelMap modelMap) {
        List<User> allUsers = userService.findAll();
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
        }
        userService.saveUser(multipartFile, user);
            return "redirect:/loginPage";
    }
    @GetMapping(value = "/users/getImage",  produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getUserImage(@RequestParam("fileName") String fileName) throws IOException {
       return userService.getImage(fileName);
    }
}



