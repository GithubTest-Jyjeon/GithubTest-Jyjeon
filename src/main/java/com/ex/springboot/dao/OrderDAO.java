package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.BoardDTO;
import com.ex.springboot.dto.OrderDTO;
import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.interfaces.IorderDAO;

@Primary
@Repository
public class OrderDAO implements IorderDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public int orderInsertProcess(String p_code_arr, String p_count_arr, int u_seq) {
		StringTokenizer tokenPcode = new StringTokenizer(p_code_arr, "|");
		StringTokenizer tokenPcount = new StringTokenizer(p_count_arr, "|");
		String p_price_arr = "";
		int p_total_price = 0;
		
		while(tokenPcode.hasMoreElements()) {
			String selectQuery = "select p_price, p_dc_percent from cg_product where p_code = '"+tokenPcode.nextElement()+"'";
			ProductDTO productDTO = (ProductDTO) template.queryForObject(selectQuery, new BeanPropertyRowMapper(ProductDTO.class));
			
			int priceCalc = productDTO.getP_price() - (productDTO.getP_price() * productDTO.getP_dc_percent()) / 100;
			
			p_total_price += priceCalc * Integer.parseInt((String) tokenPcount.nextElement());
			p_price_arr += priceCalc + "|";
		}
		
		p_price_arr = p_price_arr.substring(0, p_price_arr.length() - 1);
		
		String insertQuery = "insert into cg_order (o_seq, u_seq, p_code_arr, p_count_arr, p_price_arr, o_total_price, o_reg_date) values (ORDER_SEQ.nextval, "+u_seq+", '"+p_code_arr+"', '"+p_count_arr+"', '"+p_price_arr+"', "+p_total_price+", sysdate)";
		int result = template.update(insertQuery);
		
		String DeleteQuery = "delete from cg_cart where u_seq = "+u_seq;
		template.update(DeleteQuery);
		
		return result;
	}
	
	@Override
	public ArrayList<OrderDTO> orderListForUser(int u_seq) {
		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_ORDER ORDER BY O_SEQ DESC"
                + ") temp "
                + "WHERE ROWNUM <= 5 "
                + ") WHERE rnum > 0";

		return (ArrayList<OrderDTO>) template.query(selectQuery, new BeanPropertyRowMapper<OrderDTO>(OrderDTO.class));
	}

	
}
