package com.ex.springboot.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.GenreDTO;
import com.ex.springboot.interfaces.IkeywordDAO;

@Primary
@Repository
public class KeywordDAO implements IkeywordDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public ArrayList<GenreDTO> genreListMovie() {
		String selectQuery = "select * from cg_genre where g_code <= 45";
		
		return (ArrayList<GenreDTO>) template.query(selectQuery, new BeanPropertyRowMapper<GenreDTO>(GenreDTO.class));
	}

	@Override
	public ArrayList<GenreDTO> genreListShow() {
		String selectQuery = "select * from cg_genre where g_code >= 46";
		
		return (ArrayList<GenreDTO>) template.query(selectQuery, new BeanPropertyRowMapper<GenreDTO>(GenreDTO.class));
	}


}
