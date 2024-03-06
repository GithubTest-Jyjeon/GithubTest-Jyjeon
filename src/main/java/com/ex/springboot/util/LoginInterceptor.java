package com.ex.springboot.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userSession") != null);
        if (modelAndView != null) {
            modelAndView.addObject("isLoggedIn", isLoggedIn ? "true" : "false");
        }
    }
}

