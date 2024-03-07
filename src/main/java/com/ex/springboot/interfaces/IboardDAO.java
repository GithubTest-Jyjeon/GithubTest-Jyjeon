package com.ex.springboot.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.BoardDTO;
import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.dto.KeywordDTO;
import com.ex.springboot.dto.ResultDTO;

@Mapper
public interface IboardDAO {
	BoardDTO boardDTO = new BoardDTO();
	KeywordDTO keywordDTO = new KeywordDTO();
	ResultDTO resultDTO = new ResultDTO();
	
	public BoardDTO boardView(int b_seq);
	public int boardWrite(BoardDTO boardDTO);
	public void boardResultUpdate(int b_seq);
	public int boardUpdate(BoardDTO boardDTO);
	public int boardDelete(int b_seq);
	
	public String boardListQuery(int page, int limit, String type, String word);
	public ArrayList<BoardDTO> boardList(int page, int limit, String type, String word);
	
	public ArrayList<BoardDTO> boardListForUser(int u_seq);
	
	public int boardTotal(String type, String word);
	
	public ArrayList<KeywordDTO> keywordList(String b_keywords);
	public ArrayList<ResultDTO> resultList(String b_category, int b_seq);
	public ArrayList<GenreDTO> genreList(Object code);
	
	public void boardUpdateShare(int b_seq, String b_share_yn);
}