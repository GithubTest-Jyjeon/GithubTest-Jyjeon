package com.ex.springboot.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.interfaces.IproductDAO;

@Controller
@RequestMapping
public class ProductController {

	@Autowired
	IproductDAO dao;
	
	String[] category_arr = {"야채/채소", "축산/계란", "수산/건어물", "소스/양념/조미료", "곡류/견과류", "가공식품", "유제품", "과일류"};
	
	@GetMapping("/product")
	public String product(Model model) {
		model.addAttribute("category_arr", category_arr);
		int limit = 20;
		
		// model.addAttribute("list", dao.productNewList(limit));
		model.addAttribute("limit", limit);
		return "product/index";
	}
	
	@GetMapping("/product/index")
	public String productIndex(Model model) {
		model.addAttribute("category_arr", category_arr);
		model.addAttribute("newList", dao.productNewList());
		model.addAttribute("dcList", dao.productDcList());
		return "product/index";
	}
	
	@GetMapping("/product/list")
	public String productListPage(@RequestParam(value="p_category") String p_category, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="word", defaultValue="") String word, Model model) {
	    int limit = 24;
	    int totalCount = dao.productTotal(p_category, word);
	    int totalPage = (int) Math.ceil((double)totalCount / limit);
	    int startPage = ((page - 1) / 10) * 10 + 1;
	    int endPage = startPage + 9;
	    if(endPage > totalPage) {
	        endPage = totalPage;
	    }

	    // p_category에 해당하는 친절한 이름 찾기
	    String categoryName = p_category; // 기본값을 p_category로 설정
	    for (String category : category_arr) {
	        if (category.contains(p_category)) { // 배열에서 p_category에 해당하는 문자열을 포함하는지 확인
	            categoryName = category; // 해당하는 친절한 카테고리 이름으로 설정
	            break;
	        }
	    }
	    
	    model.addAttribute("list", dao.productList(p_category, page, limit, word));
	    model.addAttribute("category_arr", category_arr);
	    model.addAttribute("p_category", p_category);
	    model.addAttribute("categoryName", categoryName); // 모델에 친절한 카테고리 이름 추가
	    model.addAttribute("totalCount", totalCount);
	    model.addAttribute("totalPage", totalPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    model.addAttribute("page", page);
	    model.addAttribute("word", word);
	    model.addAttribute("limit", limit);
	    
	    return "product/list";
	}

	
	
	@GetMapping("/product/view")
	public String productViewPage(@RequestParam(value = "p_seq") int p_seq, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="word", defaultValue="") String word, Model model) {
		model.addAttribute("productInfo", dao.productView(p_seq));
		model.addAttribute("page", page);
		model.addAttribute("word", word);
		model.addAttribute("newList", dao.productNewList());
		model.addAttribute("dcList", dao.productDcList());
		
		return "product/view";
	}
	
	
	@GetMapping("/product/listForCategory")
	public @ResponseBody List<ProductDTO> productListForCategory(@RequestParam(value = "p_category", defaultValue = "") String p_category, Model model) {
		return dao.productListForCategory(p_category);
	}
	
}
