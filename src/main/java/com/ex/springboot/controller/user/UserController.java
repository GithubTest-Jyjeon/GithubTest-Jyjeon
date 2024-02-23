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

	@Autowired
	private SessionDAO sessionDAO;

	@GetMapping("/user/myInfoUpdate")
	public String myInfoUpdatePage(HttpServletRequest request, Model model) {
		String userId = (String) session.getAttribute("userId"); // 세션에서 userId 가져오기
		String sessionId = sessionDAO.getSessionIdByUserId(userId); // SessionDAO를 통해 sessionId 가져오기

		UserDTO userDTO = userDAO.getUserInfoBySessionId(sessionId); // UserDAO를 사용하여 사용자 정보 조회
		model.addAttribute("user", userDTO); // 조회된 사용자 정보를 모델에 추가
		return "myInfoUpdate"; // 정보를 표시할 HTML 파일 이름 반환
	}

	@PostMapping("/user/updateUserInfo")
	public String updateUserInfo(UserDTO user, HttpServletRequest request) {
		boolean updateResult = dao.updateUserInfoProcess(user);
		if(updateResult) {
			return "redirect:/user/profile"; // 업데이트 성공시 리다이렉트 할 페이지
		} else {
			return "redirect:/user/myInfoUpdate"; // 실패시 다시 수정 페이지로
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
	
}
