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
		String sql = "select * from cg_food_reply where fr_del_yn = 'N' and f_seq = "+f_seq+" order by fr_seq desc";
		ArrayList<ReplyDTO> list = (ArrayList<ReplyDTO>) template.query(sql, new BeanPropertyRowMapper<ReplyDTO>(ReplyDTO.class));
		
		return list;
	}
	
	
	@Override
	public void replyInsertProcess(int f_seq, int u_seq, String u_nickname, String fr_comment) {
		String sql = "insert into cg_food_reply (fr_seq, f_seq, u_seq, fr_reg_date, fr_comment, u_nickname, fr_del_yn) values (FOODREPLY_SEQ.nextval, "+f_seq+", "+u_seq+", sysdate, '"+fr_comment+"', '"+u_nickname+"'"
				+ ", 'N')";
		System.out.println(sql);
		template.update(sql);
	}
	
}
