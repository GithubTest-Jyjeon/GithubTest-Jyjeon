package com.ex.springboot.dto;

import lombok.Data;

@Data
public class OrderDTO {
	private int o_seq;
	private int u_seq;
	private String p_code_arr;
	private String p_count_arr;
	private String p_price_arr;
	private int o_total_price;
	private String o_reg_date;
}