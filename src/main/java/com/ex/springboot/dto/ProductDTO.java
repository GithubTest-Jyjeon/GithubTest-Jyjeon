package com.ex.springboot.dto;

import lombok.Data;

@Data
public class ProductDTO {
	private int p_seq;
	private String p_code;
	private String p_name;
	private String p_category;
	private int p_price;
	private int p_stock;
	private String p_image;
	private String p_content;
	private String p_dc_yn;
	private int p_dc_percent;
}