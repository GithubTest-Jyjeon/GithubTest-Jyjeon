package com.ex.springboot.controller.cart;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IcartDAO;
import com.ex.springboot.interfaces.IorderDAO;
import com.ex.springboot.interfaces.IproductDAO;
import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	IcartDAO daoCart;
	
	@Autowired
	IproductDAO daoProduct;
	
	@Autowired
	IuserDAO daoUser;
	
	@PostMapping("/cart/insertProduct")
	public @ResponseBody int cartInsertProduct(@RequestParam(value="p_code") String p_code, @RequestParam(value="p_count") int p_count, HttpServletRequest request) {
		ProductDTO productDTO = new ProductDTO();
		UserDTO userDTO = new UserDTO();
		
		int result = 0;
		
		HttpSession session = request.getSession();
		userDTO = (UserDTO) session.getAttribute("userSession");
		if(userDTO != null) {
			productDTO = daoProduct.productInfo(p_code);
			return daoCart.cartInsertProductProcess(p_code, p_count, userDTO);
		}else {
			return result;
		}
	}
	
	@PostMapping("/cart/updateProduct")
	public @ResponseBody int cartUpdateProduct(@RequestParam(value="p_code") String p_code, @RequestParam(value="p_count") int p_count, HttpServletRequest request) {
		ProductDTO productDTO = new ProductDTO();
		UserDTO userDTO = new UserDTO();
		
		int result = 0;
		
		HttpSession session = request.getSession();
		userDTO = (UserDTO) session.getAttribute("userSession");
		if(userDTO != null) {
			productDTO = daoProduct.productInfo(p_code);
			return daoCart.cartUpdateProductProcess(p_code, p_count, userDTO);
		}else {
			return result;
		}
	}
	
	// 장바구니 목록 페이지로 이동
	@GetMapping("/cart/list")
	public String getCartList(Model model, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
	    
	    if (userDTO != null) {
	        // 사용자 ID를 기반으로 장바구니 목록 조회
	        ArrayList<ProductDTO> cartList = daoCart.getCartListByUserId(userDTO.getU_seq());
	        model.addAttribute("cartList", cartList);
	        
	        return "/cart/list"; // 장바구니 목록을 보여주는 뷰 페이지 경로
	    } else {
	        // 사용자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트
	        return "redirect:/user/login";
	    }
	}
	
	@PostMapping("/cart/deleteProduct")
	public @ResponseBody int deleteProductFromCart(@RequestParam(value="p_code") String p_code, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
	    int result = 0;

	    if(userDTO != null) {
	        // 사용자가 로그인한 상태일 때만 삭제 처리를 진행
	        result = daoCart.deleteProductFromCart(p_code, userDTO.getU_seq());
	    }
	    
	    return result; // 성공적으로 삭제되면 1, 아니면 0 또는 오류 코드 반환
	}
	
}
