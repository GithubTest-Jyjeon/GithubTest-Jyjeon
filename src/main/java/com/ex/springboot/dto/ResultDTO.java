package com.ex.springboot.dto;

import java.util.ArrayList;

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
}