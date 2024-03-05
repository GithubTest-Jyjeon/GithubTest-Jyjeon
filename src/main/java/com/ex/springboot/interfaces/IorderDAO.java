package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IorderDAO {
    
    public int orderInsertProcess(String p_code_arr, String p_count_arr);
}
