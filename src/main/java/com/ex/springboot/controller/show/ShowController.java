package com.ex.springboot.controller.show;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dao.ShowDAO;

@Controller
@RequestMapping
public class ShowController {

	ShowDAO dao;
	
	@GetMapping("/show/list")
	public String showList(@RequestParam(value = "r_seq") int r_seq, Model model) {
		
		return "/show/list";
	}
	
	@GetMapping("/show/view")
	public String showView(@RequestParam(value = "m_code") int m_code, Model model) {
		
		return "/show/view";
	}
	
}
