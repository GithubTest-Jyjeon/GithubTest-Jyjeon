package com.ex.springboot.controller.food;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.interfaces.IfoodDAO;

@Controller
@RequestMapping
public class FoodController {

	@Autowired
	IfoodDAO dao;
	
	@GetMapping("/food/")
	public String foodMain() {
		return "/food/main";
	}
	
	@GetMapping("/food/list")
	public String foodListPage(@RequestParam(value="page", defaultValue="1") int page) {
		return "/food/list";
	}
	
	@GetMapping("/food/view")
	public String foodViewPage(int f_seq, Model model) {
		
		model.addAttribute("foodInfo", dao.foodView(f_seq));
		
		return "/food/view";
	}
}
