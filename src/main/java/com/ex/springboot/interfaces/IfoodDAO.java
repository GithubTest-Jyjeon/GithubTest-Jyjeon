package com.ex.springboot.interfaces;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.MovieDTO;

@Mapper
public interface IfoodDAO {
	FoodDTO foodDTO = new FoodDTO();
	
	public ArrayList<FoodDTO> foodList(int page, int b_seq, int u_seq);
	public FoodDTO foodView(int f_seq);
	public int foodTotal(int b_seq, int u_seq);
}