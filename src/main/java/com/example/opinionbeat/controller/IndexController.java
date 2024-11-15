package com.example.opinionbeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            String name = oauth2User.getAttribute("name");
            model.addAttribute("userName", name);
        }
        return "index";  // index.html을 보여줍니다
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "CI/CD Pipeline Test - " + new Date();
    }

}
