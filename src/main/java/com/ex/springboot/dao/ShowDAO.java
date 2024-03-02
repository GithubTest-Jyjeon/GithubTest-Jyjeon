package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.ShowDTO;
import com.ex.springboot.interfaces.IshowDAO;

@Primary
@Repository
public class ShowDAO implements IshowDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public ArrayList<ShowDTO> showList(int page, int b_seq, int u_seq) {
		String selectQuery = "select b_results from cg_board where b_seq = "+b_seq+" and u_seq = "+u_seq;
		String b_results = template.queryForObject(selectQuery, String.class);
		ArrayList<ShowDTO> list = new ArrayList<>();
		
		if(b_results != null) {
			StringTokenizer b_results_token = new StringTokenizer(b_results, "|");
			List<ShowDTO> showDTO = new ArrayList<>();
			
			while(b_results_token.hasMoreTokens()) {
				String selectQuery2 = "select t_code_arr from cg_result where r_seq = "+b_results_token.nextToken();
				String t_code_arr = template.queryForObject(selectQuery2, String.class);
				
				StringTokenizer t_code_arr_token = new StringTokenizer(t_code_arr, "|");
				while(t_code_arr_token.hasMoreTokens()) {
					String selectQuery3 = "select * from cg_show where s_code = '"+t_code_arr_token.nextToken()+"'";
					showDTO = template.query(selectQuery3, new BeanPropertyRowMapper(ShowDTO.class));
					list.addAll(showDTO);
				}
			}
		}
		return list;
	}

	@Override
	public ShowDTO showView(String s_code) {
		String selectQuery = "select * from cg_show where f_code = '"+s_code+"'";
		return template.queryForObject(selectQuery, new BeanPropertyRowMapper<ShowDTO>(ShowDTO.class));
	}

	@Override
	public int showTotal(int b_seq, int u_seq) {
		String selectQuery = "select b_total_count from cg_board where b_seq = "+b_seq+" and u_seq = "+u_seq;
		return template.queryForObject(selectQuery, Integer.class);
	}

	

}
