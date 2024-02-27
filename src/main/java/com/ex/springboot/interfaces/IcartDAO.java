package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IcartDAO {
    
    // 장바구니에 상품 추가
    int cartInsertProductProcess(@Param("p_code") 
String p_code, @Param("p_count") int p_count, @Param("userDTO") UserDTO userDTO);
    
    // 사용자 ID를 기반으로 장바구니 목록 조회
    ArrayList<ProductDTO> getCartListByUserId(@Param("userId") int userId);

    // 특정 사용자의 장바구니에서 상품을 삭제
    int deleteProductFromCart(@Param("p_code") String p_code, @Param("u_seq") int u_seq);

    // 특정 사용자의 장바구니 내 특정 상품의 수량을 업데이트
    int updateProductQuantity(@Param("p_code") String p_code, @Param("p_count") int p_count, @Param("u_seq") int u_seq);
    
    int checkProductInfo(@Param("p_code") String p_code, @Param("u_seq") int u_seq);
}
