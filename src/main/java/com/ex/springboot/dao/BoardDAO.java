package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.BoardDTO;
import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.dto.KeywordDTO;
import com.ex.springboot.dto.ResultDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;

@Primary
@Repository
public class BoardDAO implements IboardDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public BoardDTO boardView(int b_seq) {
		String updateQuery = "update cg_board set b_hit = b_hit + 1 where b_seq = ?";
		template.update(updateQuery, b_seq);

		String selectQuery = "select * from cg_board where b_seq = "+b_seq;
		
		return template.queryForObject(selectQuery, new BeanPropertyRowMapper<BoardDTO>(BoardDTO.class));
	}

	@Override
	public int boardWrite(BoardDTO boardDTO, String b_category) {
		UserDTO userDTO = new UserDTO();
		int u_seq = userDTO.getU_seq();
		
		String b_title = userDTO.getU_nickname()+" 님의 "+b_category+" 결과 공유";
		
		String insertQuery = "insert into cg_board ("
				+ "b_seq, u_seq, b_title, b_hit, b_keywords, b_content, b_reg_date, b_upd_date, b_share_yn, b_category, u_nickname"
				+ ") values ("
				+ "BOARD_SEQ.nextval, "+userDTO.getU_seq()+", 0, )";

		return u_seq;
	}

	@Override
	public int boardUpdate(BoardDTO boardDTO) {
		int b_seq = boardDTO.getB_seq();

		UserDTO userDTO = new UserDTO();
		int u_seq = userDTO.getU_seq();

		String b_content = boardDTO.getB_content();

		String updateQuery = "update CG_BOARD set b_content = ? where b_seq = ? and u_seq = ?";
		template.update(updateQuery, b_content, b_seq, u_seq);
		return 0;
	}

	@Override
	public int boardDelete(int b_seq) {
		UserDTO userDTO = new UserDTO();
		int u_seq = userDTO.getU_seq();

		String deleteQuery = "delete from CG_BOARD where b_seq = ? and u_seq = ?";
		template.update(deleteQuery, b_seq, u_seq);

		return 0;
	}

	@Override
	public List<BoardDTO> boardList(int page, int limit, String type, String word) {
		int startRow = (page - 1) * limit;
		String searchQuery = "";

		if (word.length() > 0) {
			switch (type) {
			case "title":
				searchQuery += "and B_TITLE LIKE '%" + word + "%'";
				break;
			case "content":
				searchQuery += "and B_CONTENT LIKE '%" + word + "%'";
				break;
			case "writer":
				searchQuery += "and U_NICKNAME LIKE '%" + word + "%'";
				break;
			}
		}

		// String selectQuery = "select * from cg_board";
		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_BOARD ORDER BY B_SEQ DESC"
                + ") temp "
                + "WHERE ROWNUM <= ? "+searchQuery
                + ") WHERE rnum > ?";

		return template.query(selectQuery, new Object[] { startRow + limit, startRow },
				new BeanPropertyRowMapper<BoardDTO>(BoardDTO.class));
		// return null;
	}

	@Override
	public String boardListQuery(int page, int limit, String type, String word) {
		int startRow = (page - 1) * limit;
		String searchQuery = "";

		if (word.length() > 0) {
			switch (type) {
			case "title":
				searchQuery += "and B_TITLE LIKE '%" + word + "%'";
				break;
			case "content":
				searchQuery += "and B_CONTENT LIKE '%" + word + "%'";
				break;
			case "writer":
				searchQuery += "and U_NICKNAME LIKE '%" + word + "%'";
				break;
			}
		}
		
		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_BOARD ORDER BY B_SEQ DESC"
                + ") temp "
                + "WHERE ROWNUM <= "+(startRow + limit)+" "+searchQuery
                + ") WHERE rnum > "+startRow;

		return selectQuery;
	}
	
	@Override
	public ArrayList<KeywordDTO> keywordList(String b_keywords) {
		ArrayList<KeywordDTO> list = new ArrayList<>();
		KeywordDTO item = new KeywordDTO();
		
		StringTokenizer keyword = new StringTokenizer(b_keywords, "|");
		String selectQuery = "";
		
		while(keyword.hasMoreElements()) {
			selectQuery = "select * from cg_keyword where k_code = '"+keyword.nextElement()+"'";
			item = (KeywordDTO) template.queryForObject(selectQuery, new BeanPropertyRowMapper<KeywordDTO>(KeywordDTO.class));
			list.add(item);
		}
		return list;
	}
	
	@Override
	public ArrayList<ResultDTO> resultList(String b_category, String b_results) {
		ArrayList<ResultDTO> list = new ArrayList<>();
		ArrayList<ResultDTO> item = new ArrayList<>();
		
		StringTokenizer result = new StringTokenizer(b_results, "|");
		String selectQuery = "";
		
		switch(b_category) {
			case "M" :
				while(result.hasMoreElements()) {
					selectQuery = "select m_seq as seq, m_code as code, m_title_kor as title, m_image_post as image, m_year as year, m_nation as nation, m_running_time as time from cg_movie where m_code = '"+result.nextElement()+"'";
					item = (ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class));
					list.addAll(item);
				}
				break;
				
			case "S" :
				while(result.hasMoreElements()) {
					selectQuery = "select s_seq as seq, s_code as code, s_title as title, s_image_post as image from cg_movie where m_code = '"+result.nextElement()+"'";
					item = (ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class));
					list.addAll(item);
				}
				break;
				
			case "F" :
				while(result.hasMoreElements()) {
					selectQuery = "select f_seq as seq, f_code as code, f_name as title, f_image as image from cg_movie where m_code = '"+result.nextElement()+"'";
					item = (ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class));
					list.addAll(item);
				}
				break;
		}
		return list;
	}
	
	@Override
	public ArrayList<GenreDTO> genreList(Object code) {
		System.out.println(code);
		String selectQuery = "select m_code as t_code, g_code, g_name from cg_movie_genre where m_code = '"+code+"'";
		System.out.println(selectQuery);
		ArrayList<GenreDTO> list = new ArrayList<>();
		ArrayList<GenreDTO> item = new ArrayList<>();
		item = (ArrayList<GenreDTO>) template.query(selectQuery, new BeanPropertyRowMapper<GenreDTO>(GenreDTO.class));
		list.addAll(item);
		System.out.println(list);
		return list;
	}

	@Override
	public int boardTotal(String type, String word) {
		String query = "select count(*) from CG_BOARD";
		if (word.length() > 0) {
			switch (type) {
			case "title":
				query += " where B_TITLE like '%" + word + "%'";
				break;
			case "content":
				query += " where B_CONTENT like '%" + word + "%'";
				break;
			case "writer":
				query += " where U_NICKNAME like '%" + word + "%'";
				break;
			}
		}
		return template.queryForObject(query, Integer.class);
	}

}
