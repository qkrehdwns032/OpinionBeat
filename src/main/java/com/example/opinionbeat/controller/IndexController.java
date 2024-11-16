package com.example.opinionbeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "CI/CD Pipeline Test - " + new Date();
    }

}
