package com.ex.springboot.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.ProductDTO;

@Mapper
public interface IproductDAO {
	ProductDTO productDTO = new ProductDTO();
	
	public List<ProductDTO> productList(String p_category, int page, int limit, String word);
	public String productListQuery(String p_category, int page, int limit, String word);
	public ProductDTO productView(int p_seq);
	public ProductDTO productInfo(String p_code);
	
	public int productTotal(String p_category, String word);
	public List<ProductDTO> productNewList();
	public List<ProductDTO> productDcList();
}