package com.ex.springboot.dao;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    public int cartInsertProductProcess(String p_code, int p_count, UserDTO userDTO) {
        // 상품 코드와 사용자 ID에 해당하는 항목이 있는지 확인
        String checkQuery = "SELECT COUNT(*) FROM cg_cart WHERE p_code = ? AND u_seq = ?";
        int count = template.queryForObject(checkQuery, new Object[]{p_code, userDTO.getU_seq()}, Integer.class);

        if(count > 0) {
            // 이미 항목이 있으면 수량을 업데이트
            String updateQuery = "UPDATE cg_cart SET p_count = p_count + ? WHERE p_code = ? AND u_seq = ?";
            return template.update(updateQuery, p_count, p_code, userDTO.getU_seq());
        } else {
            // 항목이 없으면 새로운 항목 삽입
            String insertQuery = "INSERT INTO cg_cart (c_seq, u_seq, p_code, p_count) VALUES (cart_seq.nextval, ?, ?, ?)";
            return template.update(insertQuery, userDTO.getU_seq(), p_code, p_count);
        }
    }

    @Override
    public ArrayList<ProductDTO> getCartListByUserId(int userId) {
        String query = "SELECT cg_product.*, cg_cart.p_count FROM cg_product LEFT JOIN cg_cart ON cg_cart.p_code = cg_product.p_code WHERE cg_cart.u_seq = ?";
        return (ArrayList<ProductDTO>) template.query(query, new Object[]{userId}, new BeanPropertyRowMapper<>(ProductDTO.class));
    }

    @Override
    public int deleteProductFromCart(String p_code, int u_seq) {
        String deleteQuery = "DELETE FROM cg_cart WHERE p_code = ? AND u_seq = ?";
        return template.update(deleteQuery, p_code, u_seq);
    }

    @Override
    public int updateProductQuantity(String p_code, int p_count, int u_seq) {
        String updateQuery = "UPDATE cg_cart SET p_count = p_count + ? WHERE p_code = ? AND u_seq = ?";
        System.out.println(updateQuery);
        return template.update(updateQuery, p_count, p_code, u_seq);
    }
    
    @Override
    public int checkProductInfo(String p_code, int u_seq) {
    	String selectQuery = "select count(*) from cg_cart where p_code = '"+p_code +"' and u_seq = "+u_seq;
    	int result = template.queryForObject(selectQuery, Integer.class);
    	return result;
    }

}

