package com.ex.springboot.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ErrorController {
	
	@RequestMapping("/error/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("에러 났다 404");
		return "error/404";
	}
	
	@RequestMapping("/error/405")
	public String errorPage405(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("에러 났다 405");
		return "error/405";
	}
	
	@RequestMapping("/error/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("에러 났다 404");
		return "error/500";
	}
	
}
