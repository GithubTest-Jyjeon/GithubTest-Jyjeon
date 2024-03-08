package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.BResultDTO;
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
	public int boardWrite(BoardDTO boardDTO) {
		String insertQuery = "insert into cg_board ("
				+ "b_seq, u_seq, b_title, b_hit, b_keywords, b_content, b_reg_date, b_upd_date, b_share_yn, b_category, u_nickname, b_total_count"
				+ ") values ("
				+ "BOARD_SEQ.nextval, "+boardDTO.getU_seq()+", '"+boardDTO.getB_title()+"', 0, null, null, sysdate, sysdate, 'N', '"+boardDTO.getB_category()+"', '"+boardDTO.getU_nickname()+"', 0)";
		int insertResult = template.update(insertQuery);
		
		int b_seq = 0;
		
		if(insertResult > 0) {
			String bSeqQuery = "select BOARD_SEQ.CURRVAL from DUAL";
			b_seq = template.queryForObject(bSeqQuery, Integer.class);
		}
		
		return b_seq;
	}
	
	@Override
	public void boardResultUpdate(int b_seq) {
		ArrayList<BResultDTO> list = new ArrayList<>();
		
		String selectQuery = "select * from cg_result where b_seq = "+b_seq;
		list = (ArrayList<BResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<BResultDTO>(BResultDTO.class));
		
		String b_result = "";
		
		for(int i = 0; i < list.size(); i++) {
			b_result += list.get(i).getR_seq()+"|";
		}
		
		if(list.size() > 0) {
			b_result = b_result.substring(0, b_result.length() - 1);
			
			String updateQuery = "update cg_board set b_results = '"+b_result+"' where b_seq = "+b_seq;
			
			template.update(updateQuery);
		}
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
	public ArrayList<BoardDTO> boardList(int page, int limit, String type, String word) {
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
                + "WHERE ROWNUM <= ? and b_share_yn = 'Y' "+searchQuery
                + ") WHERE rnum > ?";

		return (ArrayList<BoardDTO>) template.query(selectQuery, new Object[] { startRow + limit, startRow },
				new BeanPropertyRowMapper<BoardDTO>(BoardDTO.class));
	}
	
	@Override
	public ArrayList<BoardDTO> boardListForUser(int u_seq, int limit) {
		String limitQuery = "";
		if(limit > 0) {
			limitQuery = "ROWNUM <= "+limit+" AND";
		}
		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_BOARD ORDER BY B_SEQ DESC"
                + ") temp "
                + "WHERE "+limitQuery+" U_SEQ = "+u_seq
                + ") WHERE rnum > 0";

		return (ArrayList<BoardDTO>) template.query(selectQuery, new BeanPropertyRowMapper<BoardDTO>(BoardDTO.class));
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
	public ArrayList<ResultDTO> resultList(String b_category, int b_seq) {
		ArrayList<ResultDTO> list = new ArrayList<>();
		ArrayList<BResultDTO> item = new ArrayList<>();
		
		String selectQuery = "select * from cg_result where b_seq = "+b_seq;
		item = (ArrayList<BResultDTO>)template.query(selectQuery, new BeanPropertyRowMapper<BResultDTO>(BResultDTO.class));
		
		int cnt = 0;
		System.out.println("b_category : "+b_category);
		while(item.size() > cnt) {
			StringTokenizer result = new StringTokenizer(item.get(cnt).getT_code_arr(), "|");
			
			switch(b_category) {
				case "영화" :
					while(result.hasMoreElements()) {
						selectQuery = "select m_seq as seq, m_code as code, m_title_kor as title, m_image_post as image, m_year as year, m_nation as nation, m_running_time as time from cg_movie where m_code = '"+result.nextElement()+"'";
						list.addAll((ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class)));
						System.out.println("selectQuery : "+selectQuery);
					}
					break;
					
				case "공연" :
					while(result.hasMoreElements()) {
						selectQuery = "select s_seq as seq, s_code as code, s_title as title, s_image_post as image, s_region as region, s_date_s as date_s, s_date_e as date_e from cg_show where s_code = '"+result.nextElement()+"'";
						list.addAll((ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class)));
					}
					break;
					
				case "음식" :
					while(result.hasMoreElements()) {
						selectQuery = "select f_seq as seq, f_code as code, f_name as title, f_image as image, f_recipe as recipe from cg_food where f_code = '"+result.nextElement()+"'";
						list.addAll((ArrayList<ResultDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ResultDTO>(ResultDTO.class)));
					}
					break;
			}
			
			cnt++;
		}
		return list;
	}
	
	@Override
	public ArrayList<GenreDTO> genreList(Object r_seq) {
		// 수정 필요
		return null;
	}

	@Override
	public int boardTotal(String type, String word) {
		String query = "select count(*) from CG_BOARD where b_share_yn = 'Y'";
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
	
	@Override
	public void boardUpdateShare(int b_seq, String b_share_yn) {
		String updateQuery = "update cg_board set b_share_yn = '"+b_share_yn+"' where b_seq = "+b_seq;
		System.out.println(updateQuery);
		template.update(updateQuery);
	}

}
