package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.dto.KeywordDTO;

@Mapper
public interface IkeywordDAO {
	KeywordDTO keywordDTO = new KeywordDTO();
	GenreDTO genreDTO = new GenreDTO();
	
	public ArrayList<GenreDTO> genreListMovie();
	public ArrayList<GenreDTO> genreListShow();
}