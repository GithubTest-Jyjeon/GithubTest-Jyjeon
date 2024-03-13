package com.ex.springboot.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.FoodSetDTO;
import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IfoodDAO {
	FoodDTO foodDTO = new FoodDTO();
	
	public ArrayList<FoodDTO> foodList(int page, int b_seq, int u_seq);
	public FoodDTO foodView(String f_code);
	public int foodTotal(int b_seq, int u_seq);
	public ArrayList<FoodDTO> getFoodListForName(String f_name);
	public List<FoodDTO> getRandomSearchFoods();
	public FoodSetDTO getRandomFoodSet();
	public ArrayList<FoodDTO> getFoodListForCode(String f_code_arr);
	
	public int foodHeartCheck(int f_seq, int u_seq);
	public int foodHeartOn(int f_seq, int u_seq);
	public int foodHeartOff(int f_seq, int u_seq);
	
	public String foodWrite(FoodDTO foodDTO, int u_seq, String u_nickname);
	public String foodUpdate(FoodDTO foodDTO, int u_seq);
}