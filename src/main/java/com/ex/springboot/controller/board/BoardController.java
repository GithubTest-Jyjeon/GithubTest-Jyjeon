package com.ex.springboot.controller.board;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.BoardDTO;
import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.dto.KeywordDTO;
import com.ex.springboot.dto.ResultDTO;
import com.ex.springboot.interfaces.IboardDAO;

@Controller
@RequestMapping
public class BoardController {

	@Autowired
	IboardDAO dao;
	
	@RequestMapping("/board/list")
	public String BoardListPage(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="type", defaultValue="") String type, @RequestParam(value="word", defaultValue="") String word, Model model) {

		int limit = 5;
		int totalCount = dao.boardTotal(type, word);
		int totalPage = (int) Math.ceil((double)totalCount / limit);
		int startPage = ((page - 1) / 10) * 10 + 1;
		int endPage = startPage + 9;
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		
		System.out.println(dao.boardList(page, limit, type, word));
		
		model.addAttribute("list", dao.boardList(page, limit, type, word));
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("page", page);
		model.addAttribute("type", type);
		model.addAttribute("word", word);
		model.addAttribute("limit", limit);
		
		return "board/list";
	}
	
	@RequestMapping("/board/write")
	public String BoardWritePage() {
		return "board/write";
	}
	
	@RequestMapping("/board/view")
	public String BoardViewPage(@RequestParam(value="b_seq") int b_seq, @RequestParam(value="page") int page, Model model) {
		BoardDTO boardInfo = dao.boardView(b_seq);
		ArrayList<KeywordDTO> keywordList = dao.keywordList(boardInfo.getB_keywords());
		ArrayList<ResultDTO> resultList = dao.resultList(boardInfo.getB_category(), boardInfo.getB_seq());
		ArrayList<GenreDTO> genreList = new ArrayList<>();
		
		
		
		StringTokenizer code = new StringTokenizer(boardInfo.getB_results(), "|");
		// 수정 필요
//		while(code.hasMoreElements()) {
//			genreList.addAll(dao.genreList(code.nextElement()));
//		}
		
		model.addAttribute("boardInfo", boardInfo);
		model.addAttribute("keywordList", keywordList);
		model.addAttribute("resultList", resultList);
		model.addAttribute("page", page);
//		model.addAttribute("genreList", genreList);
		
		return "board/view";
	}
	
}
