package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ex.springboot.dto.OrderDTO;

@Mapper
public interface IorderDAO {
    
    public int orderInsertProcess(@Param("orderData") OrderDTO orderData);
}
