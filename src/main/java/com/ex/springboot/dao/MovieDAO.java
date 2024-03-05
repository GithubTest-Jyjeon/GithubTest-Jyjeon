package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.dto.MovieDTO;
import com.ex.springboot.interfaces.ImovieDAO;

@Primary
@Repository
public class MovieDAO implements ImovieDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public ArrayList<MovieDTO> movieList(int page, int b_seq, int u_seq) {
		String selectQuery = "select b_results from cg_board where b_seq = "+b_seq+" and u_seq = "+u_seq;
		String b_results = template.queryForObject(selectQuery, String.class);
		ArrayList<MovieDTO> list = new ArrayList<>();
		
		if(b_results != null) {
		
			StringTokenizer b_results_token = new StringTokenizer(b_results, "|");
			List<MovieDTO> movieDTO = new ArrayList();
			
			while(b_results_token.hasMoreTokens()) {
				String selectQuery2 = "select t_code_arr from cg_result where r_seq = "+b_results_token.nextToken();
				String t_code_arr = template.queryForObject(selectQuery2, String.class);
				
				int i = 0;
				StringTokenizer t_code_arr_token = new StringTokenizer(t_code_arr, "|");
				while(t_code_arr_token.hasMoreTokens()) {
					String m_code = t_code_arr_token.nextToken();
					String selectQuery3 = "select * from cg_movie where m_code = '"+m_code+"'";
					movieDTO = template.query(selectQuery3, new BeanPropertyRowMapper(MovieDTO.class));
					
					String selectQuery4 = "select g_name from cg_movie_genre where m_code = '"+m_code+"'";
					List<GenreDTO> genreDTO = (ArrayList<GenreDTO>) template.query(selectQuery4, new BeanPropertyRowMapper<GenreDTO>(GenreDTO.class));
					
					movieDTO.get(0).setG_name_arr(genreDTO);
					list.addAll(movieDTO);
				}
			}
		}
		
		return list;
	}

	 @Override
	    public MovieDTO movieView(String m_code) {
	        // 영화 정보 조회
	        String movieQuery = "select * from cg_movie where m_code = '" + m_code + "'";
	        MovieDTO movie = template.queryForObject(movieQuery, new BeanPropertyRowMapper<>(MovieDTO.class));

	        // 감독 정보 조회 
	        String directorQuery = "select d_name from cg_movie_director where m_code = '" + m_code + "'";
	        List<String> director = template.queryForList(directorQuery, String.class);
	        movie.setDirector(director);

	        // 배우 정보 조회
	        String actorQuery = "select a_name from cg_movie_actor where m_code = '" + m_code + "'";
	        List<String> actors = template.queryForList(actorQuery, String.class);
	        movie.setActors(actors);

	        // 장르 정보 조회 및 설정
	        // 이 부분은 기존의 로직을 그대로 사용합니다. 필요하다면 아래와 같이 장르 정보도 조회하여 설정할 수 있습니다.
	        String genreQuery = "select g_name from cg_movie_genre where m_code = '" + m_code + "'";
	        List<GenreDTO> genres = template.query(genreQuery, new BeanPropertyRowMapper<>(GenreDTO.class));
	        movie.setG_name_arr(genres);

	        return movie;
	    }

	@Override
	public int movieTotal(int b_seq, int u_seq) {
		String selectQuery = "select b_total_count from cg_board where b_seq = "+b_seq+" and u_seq = "+u_seq;
		return template.queryForObject(selectQuery, Integer.class);
	}

	

}
