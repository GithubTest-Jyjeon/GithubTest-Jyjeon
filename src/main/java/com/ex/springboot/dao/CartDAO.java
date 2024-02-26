package com.ex.springboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IcartDAO;

@Primary
@Repository
public class CartDAO implements IcartDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public int cartInsertProductProcess(ProductDTO productDTO, int p_count, UserDTO userDTO) {
		String insertCartList = "insert into cg_cart (c_seq, u_seq, p_code, p_count) values (cart_seq.nextval, "+userDTO.getU_seq()+", '"+productDTO.getP_code()+"', "+p_count+")";
		int result = template.update(insertCartList);
		
		return result;
	}

}
