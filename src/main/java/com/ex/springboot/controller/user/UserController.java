package com.ex.springboot.controller.user;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.SHA256;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.IorderDAO;
import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UserController {
	
	@Autowired
	IuserDAO dao;
	
	@Autowired
	IboardDAO daoBoard;
	
	@Autowired
	IorderDAO daoOrder;
	
	@GetMapping("/user/join")
	public String join() {
		return "/user/join";
	}
	
	@PostMapping("/user/joinProcess")
	public String joinProcess(UserDTO userDTO) throws NoSuchAlgorithmException {
		SHA256 sha256 = new SHA256();
		userDTO.setU_pw(sha256.encrypt(userDTO.getU_pw()));
		
		if(dao.joinProcess(userDTO)) {
			System.out.println("회원가입 성공");
			return "/user/login";
		} else{
			System.out.println("회원가입 실패");
			return "/user/join";
		}
	
	}
	
	@GetMapping("/user/login")
	public String login() {
		return "/user/login";
	}
	
	@PostMapping("/user/loginProcess")
	public String loginProcess(@RequestParam(value = "u_id") String u_id, @RequestParam(value = "u_pw") String u_pw, HttpServletRequest request) throws NoSuchAlgorithmException {
		UserDTO userDTO = new UserDTO();
		SHA256 sha256 = new SHA256();
		u_pw = sha256.encrypt(u_pw);
		
		userDTO = dao.loginProcess(u_id, u_pw);
		
		if(userDTO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userSession", userDTO);
			session.setMaxInactiveInterval(60 * 30);
			
			System.out.println("로그인 성공");
			return "redirect:/";
		}else {
			System.out.println("로그인 실패");
			return "redirect:/user/login?error=true";
		}
	}
	
	@GetMapping("/user/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		System.out.println("로그아웃 성공");
		return "redirect:/";
	}
	
	@GetMapping("/user/myPage")
	public String myPage(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if (userDTO.getU_seq() > 0) {
			UserDTO userInfo = dao.getUserInfo(userDTO.getU_seq());
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("myBoardList", daoBoard.boardListForUser(userDTO.getU_seq(), 5));
			model.addAttribute("myOrderList", daoOrder.orderListForUser(userDTO.getU_seq(), 5));
			return "/user/myPage";
		} else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/user/myInfoUpdate")
	public String myInfoUpdatePage(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if (userDTO.getU_seq() > 0) {
			UserDTO userInfo = dao.getUserInfo(userDTO.getU_seq());
			model.addAttribute("userInfo", userInfo);
			return "/user/myInfoUpdate";
		} else {
			return "/user/login";
		}
	}
	
	@PostMapping("/user/myInfoUpdateProcess")
	public String myInfoUpdateProcess(HttpServletRequest request, UserDTO userDTO, Model model) throws NoSuchAlgorithmException {
		HttpSession session = request.getSession();
		UserDTO userDTO_compare = (UserDTO) session.getAttribute("userSession");
		String nickName_before = userDTO_compare.getU_nickname();
		
		if (userDTO.getU_seq() != userDTO_compare.getU_seq()) {
			// 세션에 저장 된 u_seq 값과 전달 받은 u_seq 값이 다를 경우 로그아웃 처리
			session.invalidate();
			return "/user/login";
		}else {
			SHA256 sha256 = new SHA256();
			userDTO.setU_pw(sha256.encrypt(userDTO.getU_pw()));
			boolean result = dao.updateUserInfoProcess(userDTO);
			if(result == true) {
				session.invalidate();
				HttpSession session2 = request.getSession();
				UserDTO newUserDTO = dao.loginProcess(userDTO.getU_id(), userDTO.getU_pw());
				session2.setAttribute("userSession", newUserDTO);
				session2.setMaxInactiveInterval(60 * 30);
				String nickName_after = newUserDTO.getU_nickname();
				
				if(!nickName_before.equals(nickName_after)) {
					System.out.println(newUserDTO.getU_seq()+","+nickName_after);
					dao.nicknameChange(newUserDTO.getU_seq(), nickName_after);
				}
				model.addAttribute("msg", "회원정보가 수정 되었습니다");
				model.addAttribute("userInfo", newUserDTO);
			}
			return "/user/myInfoUpdate";
		}
	}
	
	@PostMapping("/user/userDeleteProcess")
	public @ResponseBody boolean userDeleteProcess(HttpServletRequest request, @RequestParam(value="u_seq", defaultValue="0") int u_seq, @RequestParam(value="u_pw") String u_pw) throws NoSuchAlgorithmException {
		HttpSession session = request.getSession();
		UserDTO userDTO_compare = (UserDTO) session.getAttribute("userSession");
		
		SHA256 sha256 = new SHA256();
		u_pw = sha256.encrypt(u_pw);
		
		if((u_seq == userDTO_compare.getU_seq()) && u_pw.equals(userDTO_compare.getU_pw())) {
			int result = dao.deleteUser(u_seq);
			session.invalidate();
			return result > 0;
		}else {
			return false;
		}

	}
	
	@GetMapping("/user/isUserIdExist")
	public @ResponseBody int isUserIdExist(@RequestParam(value="u_id") String u_id) {
		int result = dao.isUserIdExist(u_id);
		return result;
	}
	
	@GetMapping("/user/isUserNicknameExist")
	public @ResponseBody int isUserNicknameExist(@RequestParam(value="u_nickname") String u_nickname, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		int result = 0;
		int u_seq = 0;
		if(userDTO != null) {
			u_seq = userDTO.getU_seq();
		}
		
		result = dao.isUserNicknameExist(u_nickname, u_seq);
		
		return result;
	}
	
	
}
