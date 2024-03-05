package com.ex.springboot.controller.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IorderDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
	@Autowired
	IorderDAO dao;
	
	@ResponseBody
	@RequestMapping("/order/insert")
	public int orderInsert(@RequestParam Map<String, Object> orderData, HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		/* playerList로 넘어온 데이터를 문자열로 변환 */
		  String json = orderData.get("productInfo").toString();
		  ObjectMapper mapper = new ObjectMapper();
		  
		  List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<ArrayList<Map<String, Object>>>(){});
		  
		  String p_code_arr = "";
		  String p_count_arr = "";
		  
		  int cnt = 0;
		  while(list.size() > cnt) {
			  Map<String, Object> item = list.get(cnt);
			  
			  p_code_arr += item.get("p_code")+"|";
		  p_count_arr += item.get("p_count")+"|";
			  
			  cnt++;
		  }
		  
		  p_code_arr = p_code_arr.substring(0, p_code_arr.length() - 1);
		  p_count_arr = p_count_arr.substring(0, p_count_arr.length() - 1);
		  
		  HttpSession session = request.getSession();
		  UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		  int u_seq = userDTO.getU_seq();
		  
		  int result = dao.orderInsertProcess(p_code_arr, p_count_arr, u_seq);
		  
		  return 0;
	}
}
