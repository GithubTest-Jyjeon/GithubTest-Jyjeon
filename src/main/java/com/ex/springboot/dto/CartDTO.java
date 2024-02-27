package com.ex.springboot.dto;

import lombok.Data;

@Data
public class CartDTO {
	private int c_seq; // 장바구니 시퀀스
	private int u_seq; // 사용자 시퀀스
	private String p_code; // 상품 코드
	private int p_count; // 상품 수량
}