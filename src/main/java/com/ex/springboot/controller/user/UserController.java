package com.ex.springboot.controller.user;

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
	
		if(dao.joinProcess(userDTO)) {
			System.out.println("회원가입 성공");
			return "redirect:/user/login";
		} else{
			System.out.println("회원가입 실패");
			return "/user/join";
		}
	
	}
	
	@GetMapping("/user/myInfoUpdate")
	public String myInfoUpdatePage(UserDTO userDTO, Model model) {
		int u_seq = userDTO.getU_seq();
		
		if(u_seq > 0) {
			model.addAttribute("userInfo", userDTO);
			return "/user/myInfoUpdate";
		} else {
			return "/user/login";
		}
	}
	
	@PostMapping("/user/myInfoUpdateProcess")
	public String myInfoUpdateProcess(UserDTO userDTO) {
		if (dao.updateUserInfoProcess(userDTO)) {
			return "redirect:/";
		} else {
			return "redirect:/user/myInfoUpdate";
		}
	}
	
	@GetMapping("/user/login")
	public String login() {
		return "/user/login";
	}
	
	@PostMapping("/user/loginProcess")
	public String loginProcess(@RequestParam(value = "u_id") String u_id, @RequestParam(value = "u_pw") String u_pw, HttpServletRequest request) {
		UserDTO userDTO = new UserDTO();
		
		System.out.println("U_ID : "+u_id);
		System.out.println("U_PW : "+u_pw);
		
		userDTO = dao.loginProcess(u_id, u_pw);
		
		System.out.println(userDTO);
		
		if(userDTO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userSession", userDTO);
			session.setMaxInactiveInterval(60 * 30);
			
			System.out.println("로그인 성공");
			return "/common/main";
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
