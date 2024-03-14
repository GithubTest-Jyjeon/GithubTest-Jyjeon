package com.ex.springboot.admin.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.admin.dto.AdminProductDTO;
import com.ex.springboot.admin.interfaces.IadminProductDAO;

@Primary
@Repository
public class AdminProductDAO implements IadminProductDAO {

	@Autowired
	JdbcTemplate template;
	
	@Override
	public List<AdminProductDTO> getProductList(int page, int limit, @RequestParam(value="category", defaultValue="") String category, String p_name){
		int startRow = (page - 1) * limit;
		
		String searchQuery = "";
		if(category.length() > 0) {
			searchQuery = "and P_CATEGORY = '"+category+"'";
		}

		if (p_name.length() > 0) {
			searchQuery += "and P_NAME LIKE '%" + p_name + "%'";
		}

		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.*, ROWNUM rnum FROM ( "
                + "SELECT * FROM CG_PRODUCT ORDER BY P_NAME asc"
                + ") temp "
                + "WHERE ROWNUM <= ? "+searchQuery
                + ") WHERE rnum > ?";
		
		return template.query(selectQuery, new Object[] { startRow + limit, startRow }, new BeanPropertyRowMapper<AdminProductDTO>(AdminProductDTO.class));
	}

	@Override
	public int getProductCount(@RequestParam(value="category", defaultValue="") String category) {
		String where = "";
		if(category.length() > 0) {
			where = " where p_category = '"+category+"'";
		}
		String selectQuery = "select count(*) from cg_product"+where;
		
		return template.queryForObject(selectQuery, Integer.class);
	}
	
	@Override
    public int setProductInfo(AdminProductDTO dto) {
    	String insertQuery = ""
			+ "INSERT INTO CG_PRODUCT ("
				+ "P_SEQ, "
				+ "P_CODE, "
				+ "P_NAME, "
				+ "P_CATEGORY, "
				+ "P_PRICE, "
				+ "P_STOCK, "
				+ "P_IMAGE, "
				+ "P_CONTENT, "
				+ "P_DC_YN, "
				+ "P_DC_PERCENT, "
				+ "P_NEW_YN"
			+ ") VALUES ("
				+ "PRODUCT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
			+ ")";

        template.update(insertQuery, new Object[] {
    		dto.getP_code(),
    		dto.getP_name(),
    		dto.getP_category(),
    		dto.getP_price(),
    		dto.getP_stock(),
    		dto.getP_image(),
    		dto.getP_content(),
    		dto.getP_dc_yn(),
    		dto.getP_dc_percent(),
    		dto.getP_new_yn()
        });
        
        String returnQuery = "select max(p_seq) from cg_product";
        
        return template.queryForObject(returnQuery, Integer.class);
    }
	
	@Override
	public AdminProductDTO getProductInfo(int p_seq) {
		String selectQuery = "select * from cg_product where p_seq = ?";
		
		return template.queryForObject(selectQuery, new Object[] { p_seq }, new BeanPropertyRowMapper<AdminProductDTO>(AdminProductDTO.class));
	}
	
	@Override
    public int updProductInfo(AdminProductDTO dto) {
        String updateQuery = ""
    		+ "UPDATE cg_product SET "
	    		+ "p_code = ?, "
	    		+ "p_name = ?, "
	    		+ "p_category = ?, "
	    		+ "p_price = ?, "
	    		+ "p_stock = ?, "
	    		+ "p_image = ?, "
	    		+ "p_content = ?, "
	    		+ "p_dc_yn = ?, "
	    		+ "p_dc_percent = ?, "
	    		+ "p_new_yn = ? "
    		+ "WHERE "
    			+ "p_seq = ?";
        
        return
        template.update(updateQuery, new Object[] {
    		dto.getP_code(),
    		dto.getP_name(),
    		dto.getP_category(),
    		dto.getP_price(),
    		dto.getP_stock(),
    		dto.getP_image(),
    		dto.getP_content(),
    		dto.getP_dc_yn(),
            dto.getP_dc_percent(),
            dto.getP_new_yn(),
            dto.getP_seq()
        });
    }

	@Override
	public int delProductInfo(int p_seq) {
		String deleteQuery = "DELETE FROM cg_product WHERE p_seq = ?";
		
        return template.update(deleteQuery, p_seq);
	}
	
	@Override
	public int getLastProductSeq() {
		String selectQuery = "select max(p_seq) from cg_product";
		
		return template.queryForObject(selectQuery, Integer.class);
	}
	
	@Override
	public String getLastProductCode(String p_category) {
		String selectQuery = "SELECT * FROM ( "
                + "SELECT temp.* FROM ( "
                + "SELECT p_code FROM CG_PRODUCT where p_category = '"+p_category+"' ORDER BY P_SEQ desc"
                + ") temp "
                + "WHERE ROWNUM = 1)";
		
		return template.queryForObject(selectQuery, String.class);
	}
	
}
