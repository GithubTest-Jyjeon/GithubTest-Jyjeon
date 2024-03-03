package com.ex.springboot.dto;

import lombok.Data;

@Data
public class ResultDTO {
	private int seq;
	private String code;
	private String title;
	private String image;
	private int year;
	private String nation;
	private String time;
	private String region;
	private String date_s;
	private String date_e;
	private String recipe;
}