package com.ex.springboot.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class CommonController {

    @Autowired
    IuserDAO dao;
    
    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userSession") != null);
        model.addAttribute("isLoggedIn", isLoggedIn ? "true" : "false"); // 로그인 상태를 문자열로 모델에 추가
        return "/common/main";
    }
    
    
}
