package com.ex.springboot.dto;

import lombok.Data;

@Data
public class MovieDTO {
	private int m_seq;
	private String m_code;
	private String m_title_kor;
	private String m_title_eng;
	private int m_year;
	private String m_nation;
	private int m_running_time;
	private String m_grade;
	private String m_image_post;
	private String m_story;
	private String m_nation_type;
}