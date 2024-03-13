package com.ex.springboot.controller.user;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ex.springboot.SHA256;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.IemailDAO;
import com.ex.springboot.interfaces.IorderDAO;
import com.ex.springboot.interfaces.IuserDAO;

import ch.qos.logback.core.testUtil.RandomUtil;
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
	public String loginProcess(@RequestParam(value = "u_id") String u_id, @RequestParam(value = "u_pw") String u_pw, HttpServletRequest request, RedirectAttributes redirectAttributes) throws NoSuchAlgorithmException {
		UserDTO userDTO = new UserDTO();
		SHA256 sha256 = new SHA256();
		u_pw = sha256.encrypt(u_pw);
		
		userDTO = dao.loginProcess(u_id, u_pw);
		
		if(userDTO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userSession", userDTO);
			
			if(userDTO.getU_id().equals("admin")) {
				session.setAttribute("adminSession", userDTO);
			}
			session.setMaxInactiveInterval(60 * 30);
			
			System.out.println("로그인 성공");

			return "redirect:/";
		}else {
			System.out.println("로그인 실패");

			redirectAttributes.addFlashAttribute("error", true);
			return "redirect:/user/login";
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

	@GetMapping("/user/findId")
	public String findMyId() {
		return "/user/findId"; // 찾은 아이디를 보여줄 새로운 페이지나, 모달을 표시할 페이지
	}

	@PostMapping("/user/findIdProcess")
	public String findIdProcess(@RequestParam("u_email") String u_email, Model model) {
		int count = dao.countUserIdByEmail(u_email);

		if (count > 0) {
			UserDTO userDTO = dao.findUserByEmail(u_email); // UserDTO 객체를 반환받음
			if(userDTO != null) {
				String u_id = userDTO.getU_id(); // UserDTO 객체에서 u_id 값을 가져옴
				model.addAttribute("u_id", u_id); // u_id 값을 모델에 추가
			} else {
				// UserDTO 객체가 null인 경우 (즉, 이메일에 해당하는 사용자를 찾을 수 없는 경우)
				model.addAttribute("errorMessage", "해당 이메일로 등록된 아이디가 없습니다.");
			}
		} else {
			// 사용자를 찾을 수 없는 경우, 에러 메시지를 설정
			model.addAttribute("errorMessage", "해당 이메일로 등록된 아이디가 없습니다.");
		}

		return "/user/findId";
	}

	@Autowired
	private IemailDAO iemailDAO;

	@GetMapping("/user/findPw")
	public String findMyPw() {
		return "/user/findPw";
	}

	@PostMapping("/user/findPwProcess")
	public String findPwProcess(@RequestParam("u_id") String u_id, @RequestParam("u_email") String u_email, Model model) throws NoSuchAlgorithmException {
		int count = dao.countUserPwById(u_id, u_email);

		if (count > 0) {
			
			StringBuilder sb = new StringBuilder();
		    Random rd = new Random();

		    for(int i = 0; i < 6; i++){
		        sb.append((char)(rd.nextInt(26)+65));
		    }
		    
		    String newPw = sb.toString();
			
			System.out.println(newPw);

			SHA256 sha256 = new SHA256();
			String newPwHash = sha256.encrypt(newPw);

			dao.resetUserPw(u_id, newPwHash, u_email);

			try {
				// 초기화된 비밀번호를 이메일로 전송
				iemailDAO.sendNewPw(u_email, "비밀번호 초기화 알림", "귀하의 비밀번호가 초기화되었습니다. 새 비밀번호: " + newPw);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "비밀번호 초기화 이메일 전송에 실패했습니다.");
				return "/user/findPw"; // 이메일 전송 실패 시 돌아갈 페이지
			}

			model.addAttribute("message", "비밀번호가 초기화되었습니다. 귀하에 이메일로 초기화 비밀번호를 보내드렸습니다.");

			return "/user/login";
		} else {
			model.addAttribute("errorMessage", "해당하는 사용자 정보가 없습니다. 회원 가입을 진행해 주세요.");
			return "/user/join";
		}
	}
}
