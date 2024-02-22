package com.ex.springboot.controller.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.interfaces.IkeywordDAO;

@Controller
@RequestMapping
public class KeywordController {

	@Autowired
	IkeywordDAO dao;
	
	String prevStep = null;
	String currStep = null;
	String nextStep = null;
	
	@GetMapping("/keyword/choice")
	public String keywordChoice(@RequestParam(value = "type") String type, Model model) {
		
		model.addAttribute("type", type);
		
		return "/keyword/choice";
	}
	
	
}
