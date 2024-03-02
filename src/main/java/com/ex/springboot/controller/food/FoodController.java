package com.ex.springboot.controller.food;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IfoodDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class FoodController {

	@Autowired
	IfoodDAO dao;
	
	@GetMapping("/food/list")
	public String foodList(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "b_seq") int b_seq, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			
			int u_seq = userDTO.getU_seq();
			int limit = 1;
			int totalCount = dao.foodTotal(b_seq, u_seq);
			int totalPage = (int) Math.ceil((double)totalCount / limit);
			int startPage = ((page - 1) / 10) * 10 + 1;
			int endPage = startPage + 9;
			if(endPage > totalPage) {
				endPage = totalPage;
			}
			
			model.addAttribute("list", dao.foodList(page, b_seq, u_seq));
			model.addAttribute("totalCount", totalCount);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("page", page);
			model.addAttribute("limit", limit);
			model.addAttribute("b_seq", b_seq);
			
			return "/food/list";
		}else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/food/view")
	public String foodView(@RequestParam(value = "f_code") String f_code, Model model) {
		
		model.addAttribute("foodInfo", dao.foodView(f_code));
		model.addAttribute("f_code", f_code);
		
		System.out.println("f_code : "+f_code);
		System.out.println("foodInfo : ");
		System.out.println(dao.foodView(f_code));
		
		return "/food/view";
	}
}
