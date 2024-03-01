package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.MovieDTO;

@Mapper
public interface ImovieDAO {
	MovieDTO movieDTO = new MovieDTO();
	
	public ArrayList<MovieDTO> movieList(int page, int b_seq, int u_seq);
	public MovieDTO movieView(int b_seq);
	public int movieTotal(int b_seq, int u_seq);
}