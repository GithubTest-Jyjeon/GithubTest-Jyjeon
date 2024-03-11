package com.ex.springboot.dto;

import lombok.Data;

@Data
public class ReplyDTO {
	private int fr_seq;
	private int u_seq;
	private int f_seq;
	private String u_nickname;
	private String fr_reg_date;
	private String fr_comment;
}