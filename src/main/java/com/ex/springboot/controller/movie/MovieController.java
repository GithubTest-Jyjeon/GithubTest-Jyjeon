package com.ex.springboot.controller.movie;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.MovieDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.ImovieDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class MovieController {

	@Autowired
	ImovieDAO dao;
	
	@Autowired
	IboardDAO daoBoard;
	
	@GetMapping("/movie/list")
	public String movieList(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "b_seq") int b_seq, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			
			int u_seq = userDTO.getU_seq();
			int limit = 1;
			int totalCount = dao.movieTotal(b_seq, u_seq);
			int totalPage = (int) Math.ceil((double)totalCount / limit);
			int startPage = ((page - 1) / 10) * 10 + 1;
			int endPage = startPage + 9;
			if(endPage > totalPage) {
				endPage = totalPage;
			}
			
			ArrayList<MovieDTO> list = dao.movieList(page, b_seq, u_seq);
			
			model.addAttribute("list", list);
			model.addAttribute("totalCount", totalCount);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("page", page);
			model.addAttribute("limit", limit);
			model.addAttribute("b_seq", b_seq);
			model.addAttribute("boardInfo", daoBoard.boardView(b_seq));
			
			return "movie/list";
		}else {
			return "redirect:/user/login";
		}
	}
	
	 @GetMapping("/movie/view")
	    public String movieView(@RequestParam(value = "m_code") String m_code, Model model) {
	        MovieDTO movieInfo = dao.movieView(m_code);
	        
	        // 배우 목록을 쉼표로 구분된 문자열로 변환
	        String actorsString = String.join(", ", movieInfo.getActors());
	        // 감독 목록을 쉼표로 구분된 문자열로 변환
	        String directorString = String.join(", ", movieInfo.getDirector());
	        
	        // 장르 목록을 쉼표로 구분된 문자열로 변환
	        String genresString = movieInfo.getG_name_arr().stream()
	                                       .map(genre -> genre.getG_name()) // GenreDTO에서 장르 이름을 추출
	                                       .collect(Collectors.joining(", ")); // 쉼표로 구분하여 결합
	        
	        model.addAttribute("movieInfo", movieInfo);
	        model.addAttribute("actorsString", actorsString);
	        model.addAttribute("genresString", genresString); // 변환된 장르 문자열을 모델에 추가
	        model.addAttribute("directorString", directorString);
	        model.addAttribute("m_code", m_code);
	        
	        return "movie/view";
	    }

	
}
