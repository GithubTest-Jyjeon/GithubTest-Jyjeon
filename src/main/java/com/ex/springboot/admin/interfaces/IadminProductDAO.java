package com.ex.springboot.admin.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.admin.dto.AdminProductDTO;

@Mapper
public interface IadminProductDAO {

	public List<AdminProductDTO> getProductList(int page, int limit, String category, String p_name);
	public int getProductCount(String category);
	public int setProductInfo(AdminProductDTO dto);
	public AdminProductDTO getProductInfo(int p_seq);
	public int updProductInfo(AdminProductDTO dto);
	public int delProductInfo(int p_seq);
	public int getLastProductSeq();
	public String getLastProductCode(String p_category);
}
