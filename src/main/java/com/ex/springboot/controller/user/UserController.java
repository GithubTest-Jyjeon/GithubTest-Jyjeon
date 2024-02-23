package com.ex.springboot.controller.user;

import java.util.ArrayList;
import java.util.List;

import com.ex.springboot.dao.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dao.UserDAO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class UserController {

	@Autowired
	private UserDAO dao;

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
	public String myInfoUpdate(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Integer userSeq = (Integer) session.getAttribute("ss_u_seq");
		System.out.println("testing "+userSeq);
		if (userSeq != null) {
			UserDTO userInfo = dao.getUserInfo(userSeq);
			model.addAttribute("userInfo", userInfo);
			return "user/myInfoUpdate";
		} else {
			return "redirect:/user/login";
		}
	}

	@PostMapping("/user/myInfoUpdateProcess")
	public String myInfoUpdateProcess(UserDTO userDTO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer userSeq = (Integer) session.getAttribute("ss_u_seq");
		if (userSeq != null && dao.updateUserInfoProcess(userDTO)) {
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
		UserDTO userInfo = dao.loginProcess(u_id, u_pw);

		if(userInfo != null) {
			HttpSession session = request.getSession();
			session.setAttribute("ss_u_seq", userInfo.getU_seq());
			System.out.println(session.getAttribute("ss_u_seq"));
			System.out.println("로그인 성공");
			return "redirect:/";
		} else {
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

	@PostMapping("/user/deleteUserProcess")
	public String deleteUserProcess(@RequestParam(value = "u_seq") Integer u_seq, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		Integer userSeq = (Integer) session.getAttribute("ss_u_seq");

		if (userSeq != null && userSeq.equals(u_seq)) {
			boolean isDeleted = dao.deleteUser(u_seq);
			if (isDeleted) {
				session.invalidate(); // 세션 무효화
				redirectAttributes.addFlashAttribute("message", "성공적으로 탈퇴되었습니다.");
				return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
			} else {
				redirectAttributes.addFlashAttribute("error", "회원 탈퇴에 실패하였습니다.");
				return "redirect:/user/mypage"; // 마이페이지 또는 이전 페이지로 리다이렉트
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "잘못된 접근입니다.");
			return "redirect:/user/mypage"; // 마이페이지 또는 이전 페이지로 리다이렉트
		}
	}
}
