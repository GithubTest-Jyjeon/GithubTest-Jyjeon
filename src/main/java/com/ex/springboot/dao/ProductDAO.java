package com.ex.springboot.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.interfaces.IproductDAO;

@Primary
@Repository
public class ProductDAO implements IproductDAO {

	@Autowired
	JdbcTemplate template;
	
	@Override
	public List<ProductDTO> productList(String p_category, int page, int limit, String word){
		int startRow = (page - 1) * limit;
		
		String searchQuery = "";
		if(p_category != "") {
			searchQuery = "and P_CATEGORY = '"+p_category+"'";
		}

		if (word.length() > 0) {
			searchQuery += "and P_NAME LIKE '%" + word + "%'";
		}

		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_PRODUCT ORDER BY P_SEQ DESC"
                + ") temp "
                + "WHERE ROWNUM <= ? "+searchQuery
                + ") WHERE rnum > ?";
		
		return template.query(selectQuery, new Object[] { startRow + limit, startRow }, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}
	
	@Override
	public List<ProductDTO> productNewList(){

		String selectQuery = "select * from cg_product where p_new_yn = 'Y'";
		
		return template.query(selectQuery, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}
	
	@Override
	public List<ProductDTO> productDcList(){

		String selectQuery = "select * from cg_product where p_dc_yn = 'Y'";
		
		return template.query(selectQuery, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}
	
	@Override
	public String productListQuery(String p_category, int page, int limit, String word){
		int startRow = (page - 1) * limit;
		String searchQuery = "and p_category = '"+p_category+"'";

		if (word.length() > 0) {
			searchQuery += "and P_NAME LIKE '%" + word + "%'";
		}

		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_PRODUCT ORDER BY P_SEQ DESC"
                + ") temp "
                + "WHERE ROWNUM <= "+(startRow + limit)+" "+searchQuery
                + ") WHERE rnum > "+startRow;
		
		return selectQuery;
	}

	@Override
	public ProductDTO productView(int p_seq) {
		String selectQuery = "select * from cg_product where p_seq = "+p_seq;
			
		return template.queryForObject(selectQuery, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}
	
	@Override
	public ProductDTO productInfo(String p_code) {
		String selectQuery = "select * from cg_product where p_code = '"+p_code+"'";
		
		return template.queryForObject(selectQuery, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}

	@Override
	public int productTotal(String p_category, String word) {
		String where = "";
		if(word.length() > 0) {
			where = "and p_name like '%"+word+"%'";
		}
		String selectQuery = "select count(*) from cg_product where p_category = '"+p_category+"' "+where;
		
		return template.queryForObject(selectQuery, Integer.class);
	}

	

}
