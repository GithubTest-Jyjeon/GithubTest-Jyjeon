package com.ex.springboot.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCommonController {

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard"; // dashboard.html 뷰를 반환
    }

}
