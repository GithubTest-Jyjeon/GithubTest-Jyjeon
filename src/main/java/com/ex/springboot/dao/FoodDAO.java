package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.interfaces.IfoodDAO;

@Primary
@Repository
public class FoodDAO implements IfoodDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public ArrayList<FoodDTO> foodResult(Map<String, String> list) {
		
		String selectQuery = "select * from cg_food";
		
		System.out.println("data : "+list.get("targetTable"));
		System.out.println("data : "+list.get("data"));

		
		
		System.out.println(selectQuery);
		
//		item = (ArrayList<FoodDTO>) template.query(selectQuery, new BeanPropertyRowMapper<FoodDTO>(FoodDTO.class));
//		list.addAll(item);
//		System.out.println(list);
//		return list;
		return null;
	}

	@Override
	public FoodDTO foodView(int f_seq) {
		// TODO Auto-generated method stub
		return null;
	}

}
