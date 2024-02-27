package com.ex.springboot.controller.keyword;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.dto.BoardDTO;
import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.IkeywordDAO;
import com.ex.springboot.interfaces.IresultDAO;

@Controller
@RequestMapping
public class KeywordController {

	@Autowired
	IkeywordDAO daoKeyword;
	
	@Autowired
	IboardDAO daoBoard;
	
	@Autowired
	IresultDAO daoResult;
	
	String prevStep = null;
	String currStep = null;
	String nextStep = null;
	
	@GetMapping("/keyword/choice")
	public String keywordChoice(@RequestParam(value = "type") String type, Model model) {
		
		ArrayList<GenreDTO> genreListMovie = daoKeyword.genreListMovie();
		ArrayList<GenreDTO> genreListShow = daoKeyword.genreListShow();
		
		model.addAttribute("type", type);
		model.addAttribute("genreListMovie", genreListMovie);
		model.addAttribute("genreListShow", genreListShow);
		
		return "/keyword/choice";
	}
	
	
	@PostMapping("/keyword/makeResult")
	public @ResponseBody String keywordMakeResult(
			@RequestParam(value="targetTable") String targetTable,
			@RequestParam(value="theme", defaultValue="all") String theme,
			@RequestParam(value="main", defaultValue="all") String main,
			@RequestParam(value="soup", defaultValue="all") String soup,
			@RequestParam(value="spicy", defaultValue="all") String spicy,
			@RequestParam(value="nation", defaultValue="all") String nation,
			@RequestParam(value="genre", defaultValue="all") String genre,
			@RequestParam(value="year", defaultValue="all") String year,
			@RequestParam(value="region", defaultValue="all") String region,
			Model model
		){
		
		int b_seq = 0;
		BoardDTO boardDTO = new BoardDTO();
		String b_category = "";
		
		switch(targetTable) {
			case "cg_movie" : b_category = "영화"; break;
			case "cg_show" : b_category = "공연"; break;
			case "cg_food" : b_category = "음식"; break;
		}
		
		b_seq = daoBoard.boardWrite(boardDTO, b_category);
		
		switch(targetTable) {
			case "CG_MOVIE" :
				daoResult.makeMovieResult(b_seq, targetTable, genre, nation, year);
				break;
			case "CG_SHOW" :
				daoResult.makeShowResult(b_seq, targetTable, genre, region);
				break;
			case "CG_FOOD" :
				b_seq = daoResult.makeFoodResult(b_seq, targetTable, theme, main, soup, spicy);
				break;
		}
		
		// model.addAttribute("resultList", daoBoard.boardWrite(null));
		
		
//		model.addAttribute("foodResult", dao.foodResult(list));
//		model.addAttribute("page", 1);
//		
//		return "/food/list";
		
		// return targetTable;
		return null;
	}
	
	
}
