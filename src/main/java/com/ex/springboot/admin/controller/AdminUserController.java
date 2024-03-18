package com.ex.springboot.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ex.springboot.admin.dao.AdminUserDAO;
import com.ex.springboot.admin.dto.AdminUserDTO;
import com.ex.springboot.dao.UserDAO;
import com.ex.springboot.dto.UserDTO;

@Controller
public class AdminUserController {

	@Autowired
	AdminUserDAO dao;
	
	@GetMapping("/admin/user/list")
    public String listUsers(Model model) {
        List<AdminUserDTO> userList = dao.getAllUsers(); // 수정된 getAllUsers() 호출
        model.addAttribute("users", userList);
        return "/admin/userList"; // 사용자 목록 페이지
    }

    @GetMapping("/admin/user/deleteUser")
    public String deleteUser(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = dao.deleteUser(u_seq); // 삭제 또는 탈퇴 처리
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제(또는 탈퇴 처리) 성공");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제(또는 탈퇴 처리) 실패");
        }
        return "redirect:/admin/user/list";
    }
    
    @GetMapping("/admin/user/deleteUserCompletely")
    public String deleteUserCompletely(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = dao.deleteUserCompletely(u_seq);
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자가 완전히 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제 실패.");
        }
        return "redirect:/admin/user/list";
    }
    
    @GetMapping("/admin/user/reactivateUser")
    public String reactivateUser(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = dao.reactivateUser(u_seq);
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자가 성공적으로 재활성화되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 재활성화 실패.");
        }
        return "redirect:/admin/user/list";
    }


}
