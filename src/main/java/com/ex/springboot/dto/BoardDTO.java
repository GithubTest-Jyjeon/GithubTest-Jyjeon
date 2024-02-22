package com.ex.springboot.dto;

import lombok.Data;

@Data
public class BoardDTO {
	private int b_seq;
	private int u_seq;
	private String b_title;
	private int b_hit;
	private String b_keywords;
	private String b_results;
	private String b_content;
	private String b_reg_date;
	private String b_upd_date;
	private String b_share_yn;
	private String b_category;
	private String u_nickname;
}