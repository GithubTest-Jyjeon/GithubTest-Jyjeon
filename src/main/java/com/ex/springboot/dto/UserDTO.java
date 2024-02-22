package com.ex.springboot.dto;

import lombok.Data;

@Data
public class UserDTO {
	private int u_seq;
	private String u_id;
	private String u_pw;
	private String u_name;
	private String u_nickname;
	private String u_email;
	private String u_birth;
	private String u_postcode;
	private String u_address;
	private String u_address_detail;
	private String u_address_extra;
	private String u_gender;
	private String u_phone;
	private String u_reg_date;
	private String u_upd_date;
	private String u_log_date;
	private String u_del_date;
	private String u_del_yn;
}