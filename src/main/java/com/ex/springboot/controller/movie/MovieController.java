package com.ex.springboot.controller.movie;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class MovieController {

//	@Autowired
//	ImovieDAO dao;
	
//	@GetMapping("/movie/list")
//	public String movieList(@RequestParam(value = "r_seq") int r_seq, Model model) {
//		
//		return "/movie/list";
//	}
	
//	@GetMapping("/movie/view")
//	public String movieView(@RequestParam(value = "m_code") int m_code, Model model) {
//		
//		return "/movie/view";
//	}
	
}
