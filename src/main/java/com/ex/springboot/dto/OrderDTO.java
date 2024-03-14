package com.ex.springboot.dto;

import java.util.List;
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
    private List<String> items; // 상품명, 수량, 가격 정보를 담은 리스트
}
