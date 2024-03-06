package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.OrderDTO;

@Mapper
public interface IorderDAO {
    
    public int orderInsertProcess(String p_code_arr, String p_count_arr, int u_seq);
    public ArrayList<OrderDTO> orderListForUser(int u_seq);
}
