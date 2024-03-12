package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.FoodDTO;
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

	
	@Override
	public List<ProductDTO> productListForCategory(String p_category){
		ArrayList<ProductDTO> list = new ArrayList<>();
		
		String sql = "select * from cg_product where p_category = '"+p_category+"' order by p_name asc";
		list = (ArrayList<ProductDTO>) template.query(sql, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
		
		return list;
	}
	
	
	//전체 물품 가져오기
	@Override
	public List<ProductDTO> getAllProducts(){
	    String selectQuery = "SELECT * FROM cg_product ORDER BY p_code ASC";
	    return template.query(selectQuery, new BeanPropertyRowMapper<ProductDTO>(ProductDTO.class));
	}
	
	//상품 삭제
    public int deleteProduct(int p_seq) {
        String deleteQuery = "DELETE FROM cg_product WHERE p_seq=?";
        return template.update(deleteQuery, p_seq);
    }
	
 // 상품 정보 업데이트
    public int updateProduct(ProductDTO product) {
        String updateQuery = "UPDATE cg_product SET p_code=?, p_name=?, p_category=?, p_price=?, p_stock=?, p_image=?, p_content=?, p_dc_yn=?, p_dc_percent=?, p_new_yn=? WHERE p_seq=?";
        System.out.println(updateQuery);
        System.out.println(product);
        return template.update(updateQuery,
                product.getP_code(),
                product.getP_name(),
                product.getP_category(),
                product.getP_price(),
                product.getP_stock(),
                product.getP_image(),
                product.getP_content(),
                product.getP_dc_yn(),
                product.getP_dc_percent(),
                product.getP_new_yn(),
                product.getP_seq());
    }
    
    
    public int insertProduct(ProductDTO product) {
    	String insertQuery = "INSERT INTO CG_PRODUCT (P_SEQ, P_CODE, P_NAME, P_CATEGORY, P_PRICE, P_STOCK, P_IMAGE, P_CONTENT, P_DC_YN, P_DC_PERCENT, P_NEW_YN) " +
                "VALUES (PRODUCT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return template.update(insertQuery, product.getP_code(), product.getP_name(), product.getP_category(), product.getP_price(), product.getP_stock(), product.getP_image(), product.getP_content(), product.getP_dc_yn(), product.getP_dc_percent(), product.getP_new_yn());
    }







}
