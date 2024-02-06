package com.ex.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dao.IuserDAO;
import com.ex.springboot.dto.userDTO;

@Controller
@RequestMapping
public class chugetController {

	@Autowired
	IuserDAO dao;
	
	@GetMapping("/")
	public String main() {
		return "common/main";
	}
	
	@GetMapping("/board")
	public String board() {
		return "board/list";
	}
	
//	@GetMapping("/best")
//	public String bestPage(Model model) {
//		ItemDto itemDto = new ItemDto();
//		// itemDto.setId();
//		itemDto.setItemDetail("베스트 상품 상세 설명");
//		itemDto.setItemNm("베스트 상품 1");
//		itemDto.setPrice(10000);
//		itemDto.setRegTime(LocalDateTime.now());
//		
//		model.addAttribute("itemDto", itemDto);
//		return "common/best";
//	}
//	
//	@GetMapping("/bestProducts")
//	public String bestProductsPage(Model model) {
//		List<ItemDto> itemDtoList = new ArrayList<>();
//		
//		for(int i = 1; i <= 10; i++) {
//			ItemDto itemDto = new ItemDto();
//			itemDto.setItemDetail("베스트 상품 상세 설명 "+i);
//			itemDto.setItemNm("베스트 상품 "+i);
//			itemDto.setPrice(1000 * i);
//			itemDto.setRegTime(LocalDateTime.now());
//			
//			itemDtoList.add(itemDto);
//		}
//		
//		model.addAttribute("itemDtoList", itemDtoList);
//		return "common/bestProducts";
//	}
	
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}
	
	@PostMapping("/loginProcess")
	public String loginProcess(@RequestParam(value = "u_id") String u_id, @RequestParam(value = "u_pw") String u_pw, Model model) {
		List<userDTO> userDTO = new ArrayList<>();
		 
		System.out.println("U_ID : "+u_id);
		System.out.println("U_PW : "+u_pw);
		
		userDTO = dao.loginDAO(u_id, u_pw);
		
		if(userDTO.size() > 0) {
			System.out.println("굿");
			return "redirect:/";
		}else {
			return "login";
		}
	}
	
//	@PostMapping("/loginProcess")
//	public String loginProcess(@RequestParam(value = "id") String id, @RequestParam(value = "pw") String pw, Model model) {
//		int result = 0;
//		result = funcLoginProcess(id, pw);
//		return result;
//	}
	
}
