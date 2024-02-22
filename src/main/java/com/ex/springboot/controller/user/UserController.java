package com.ex.springboot.controller.user;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UserController {

	@Autowired
	IuserDAO dao;
	
	@GetMapping("/user/join")
	public String join() {
		return "/user/join";
	}
	
	@PostMapping("/user/joinProcess")
	public String joinProcess(UserDTO userDTO) {
	
		if(dao.joinDAO(userDTO)) {
			System.out.println("회원가입 성공");
			return "redirect:/user/login";
		} else{
			System.out.println("회원가입 실패");
			return "/user/join";
		}
	
	}
	
	@GetMapping("/user/myInfoUpdate")
	public String myInfoUpdatePage(HttpServletRequest request, Model model) {
		List<UserDTO> userDTO = new ArrayList<>();
		
		HttpSession session = request.getSession();
		int u_seq = (int) session.getAttribute("ss_u_seq");
		
		if(u_seq > 0) {
			userDTO = dao.myInfoDAO(u_seq);
			UserDTO userInfo = userDTO.get(0);
			model.addAttribute("userInfo", userInfo);
			return "/user/myInfoUpdate";
		} else {
			return "/user/login";
		}
	}
	
	@GetMapping("/user/login")
	public String login() {
		return "/user/login";
	}
	
	@PostMapping("/user/loginProcess")
	public String loginProcess(@RequestParam(value = "u_id") String u_id, @RequestParam(value = "u_pw") String u_pw, HttpServletRequest request) {
		List<UserDTO> userDTO = new ArrayList<>();
		
		System.out.println("U_ID : "+u_id);
		System.out.println("U_PW : "+u_pw);
		
		userDTO = dao.loginDAO(u_id, u_pw);
		
		UserDTO userInfo = userDTO.get(0);
		
		if(userDTO.size() > 0) {
			HttpSession session = request.getSession();
			
			session.setAttribute("ss_u_seq", userInfo.getU_seq());
			System.out.println("로그인 성공");
			return "redirect:/";
		}else {
			System.out.println("로그인 실패");
			return "/user/login";
		}
	}
	
	@GetMapping("/user/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		System.out.println("로그아웃 성공");
		return "redirect:/";
	}
	
}
