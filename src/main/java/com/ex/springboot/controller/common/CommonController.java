package com.ex.springboot.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ex.springboot.interfaces.IuserDAO;

@Controller
@RequestMapping
public class CommonController {

	@Autowired
	IuserDAO dao;
	
	@GetMapping("/")
	public String main(Model model) {
		return "/common/main";
	}
}
