package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IresultDAO {
	public int makeMovieResult(int b_seq, String targetTable, String genre, String nation, String year);
	public int makeShowResult(int b_seq, String targetTable, String genre, String region);
	public int makeFoodResult(int b_seq, String targetTable, String theme, String main, String soup, String spicy);	
}