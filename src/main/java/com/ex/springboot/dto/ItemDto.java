package com.ex.springboot.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ItemDto {
	private Long id;
	private Integer price;
	private String itemNm;
	private String itemDetail;
	private String sellStatCd;
	private LocalDateTime regTime;
	private LocalDateTime updateTime;
}