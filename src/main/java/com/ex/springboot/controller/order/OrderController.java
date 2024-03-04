package com.ex.springboot.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ex.springboot.dto.OrderDTO;
import com.ex.springboot.interfaces.IorderDAO;

@Controller
public class OrderController {
	
	@Autowired
	IorderDAO dao;
	
	@RequestMapping("/order/insert")
	public String orderInsertProcess(OrderDTO orderData) {
		dao.orderInsertProcess(orderData);
		System.out.println("orderData : "+orderData);
		
		return "/order/view";
	}
}
