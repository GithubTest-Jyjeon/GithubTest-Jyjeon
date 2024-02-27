package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IcartDAO {
	ProductDTO productDTO = new ProductDTO();
	
	public int cartInsertProductProcess(ProductDTO productDTO, int p_count, UserDTO userDTO);
}