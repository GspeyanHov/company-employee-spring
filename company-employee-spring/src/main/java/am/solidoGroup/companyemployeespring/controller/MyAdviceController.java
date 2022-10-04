package am.solidoGroup.companyemployeespring.controller;

import am.solidoGroup.companyemployeespring.entity.User;
import am.solidoGroup.companyemployeespring.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class MyAdviceController {

    @ModelAttribute(name = "currentUser")
    public User getCurrentUser(@AuthenticationPrincipal CurrentUser currentUser){
        if(currentUser != null){
           return currentUser.getUser();
        }
        return null;
    }
}
