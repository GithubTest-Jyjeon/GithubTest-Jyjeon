package com.ex.springboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.interfaces.IheartDAO;

@Primary
@Repository
public class HeartDAO implements IheartDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public int getHeartCount(int f_seq) {
		String sql = "select count(*) from cg_food_heart where f_seq = "+f_seq;
		int cnt = template.queryForObject(sql, Integer.class);
		if(cnt > 0 ) {
			return cnt;
		}else {
			return 0;
		}
	}


	
}
