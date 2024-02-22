package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.FoodDTO;

@Mapper
public interface IfoodDAO {
	FoodDTO foodDTO = new FoodDTO();
	
	public FoodDTO foodView(int f_seq);
	
}