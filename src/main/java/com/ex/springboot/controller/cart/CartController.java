package com.ex.springboot.controller.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IcartDAO;
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
			return daoCart.cartInsertProductProcess(productDTO, p_count, userDTO);
		}else {
			return result;
		}
	}
}
