package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.KeywordDTO;
import com.ex.springboot.dto.MovieDTO;
import com.ex.springboot.dto.ShowDTO;

@Mapper
public interface IkeywordDAO {
	KeywordDTO keywordDTO = new KeywordDTO();
	
	public ArrayList<MovieDTO> keywordListMovie(int step);
	public ArrayList<ShowDTO> keywordListShow(int step);
}