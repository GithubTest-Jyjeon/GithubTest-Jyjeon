package com.ex.springboot.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.ReplyDTO;
import com.ex.springboot.interfaces.IreplyDAO;

@Primary
@Repository
public class ReplyDAO implements IreplyDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public int getReplyCount(int f_seq) {
		String sql = "select count(*) from cg_food_reply where fr_del_yn = 'N' and f_seq = "+f_seq;
		int cnt = template.queryForObject(sql, Integer.class);
		if(cnt > 0 ) {
			return cnt;
		}else {
			return 0;
		}
	}
	
	@Override
	public ArrayList<ReplyDTO> getReplyList(int f_seq) {
		String sql = "select * from cg_food_reply where fr_del_yn = 'N' and f_seq = "+f_seq;
		ArrayList<ReplyDTO> list = (ArrayList<ReplyDTO>) template.query(sql, new BeanPropertyRowMapper<ReplyDTO>(ReplyDTO.class));
		
		return list;
	}


	
}
