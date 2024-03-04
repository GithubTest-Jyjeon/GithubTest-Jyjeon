package com.ex.springboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.OrderDTO;
import com.ex.springboot.interfaces.IorderDAO;

@Primary
@Repository
public class OrderDAO implements IorderDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public int orderInsertProcess(OrderDTO orderData) {
		System.out.println("orderData : "+orderData);
		return 0;
	}

	
}
