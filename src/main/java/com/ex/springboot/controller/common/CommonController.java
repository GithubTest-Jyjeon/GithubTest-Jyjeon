package com.ex.springboot.controller.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IuserDAO;

@Controller
@RequestMapping
public class CommonController {

	@Autowired
	IuserDAO dao;
	
	@GetMapping("/")
	public String main() {
		return "/common/main";
	}
}
