package com.ex.springboot.dto;

import lombok.Data;

@Data
public class FoodDTO {
	private int f_seq;
	private String f_code;
	private String f_name;
	private String f_image;
	private String f_code_arr;
	private String f_voulum_arr;
	private String f_recipe;
	private String f_type_theme;
	private String f_type_main;
	private String f_type_soup;
	private String f_type_spicy;
}