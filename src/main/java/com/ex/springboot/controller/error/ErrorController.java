package com.ex.springboot.controller.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ErrorController {
	
	@RequestMapping("/error/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> item = new HashMap<>();
		ArrayList<Map<String, Object>> list = new ArrayList<>();
		
		int lotto[] = new int [6];
		String color[] = new String [6];
		
		for(int i = 0; i < 6; i++) {
			lotto[i] = (int)(Math.random() * 45) + 1;

			for(int j = 0; j < i; j++) {
				if(lotto[i] == lotto[j]) {
					i--;
					break;
				}
			}
		}
		
		for(int k = 0; k < 6; k++) {
			if(lotto[k] <= 10) {
				color[k] = "lottoYellow";
			}else if(lotto[k] <= 20) {
				color[k] = "lottoBlue";
			}else if(lotto[k] <= 30) {
				color[k] = "lottoRed";
			}else if(lotto[k] <= 40) {
				color[k] = "lottoGray";
			}else {
				color[k] = "lottoGreen";
			}
			
			item.put("lottoNumber", lotto[k]);
			item.put("lottoColor", color[k]);
			System.out.println(item);
			
			System.out.println(item);
		}
		
		
		model.addAttribute("lottoNumber", lotto);
		model.addAttribute("lottoColor", color);
//		model.addAttribute("lottoList", map);
		
		model.addAttribute("lottoList", list);
		
		return "/error/404";
	}
	
	@RequestMapping("/error/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		return "/error/500";
	}
	
}
