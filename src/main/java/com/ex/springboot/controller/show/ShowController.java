package com.ex.springboot.controller.show;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IshowDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class ShowController {

	@Autowired
	IshowDAO dao;
	
	@GetMapping("/show/list")
	public String showList(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "b_seq") int b_seq, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
		
			int u_seq = userDTO.getU_seq();
			int limit = 1;
			int totalCount = dao.showTotal(b_seq, u_seq);
			int totalPage = (int) Math.ceil((double)totalCount / limit);
			int startPage = ((page - 1) / 10) * 10 + 1;
			int endPage = startPage + 9;
			if(endPage > totalPage) {
				endPage = totalPage;
			}
			
			model.addAttribute("list", dao.showList(page, b_seq, u_seq));
			model.addAttribute("totalCount", totalCount);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("page", page);
			model.addAttribute("limit", limit);
			model.addAttribute("b_seq", b_seq);
			
			return "/show/list";
		}else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/show/view")
	public String showView(@RequestParam(value = "s_code") String s_code, Model model) {
		
		model.addAttribute("showInfo", dao.showView(s_code));
		model.addAttribute("s_code", s_code);
		
		return "/show/view";
	}
	
}
