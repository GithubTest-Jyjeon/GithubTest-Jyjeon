package com.ex.springboot.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.interfaces.IproductDAO;

@Controller
@RequestMapping
public class ProductController {

	@Autowired
	IproductDAO dao;
	
	String[] category_arr = {"야채/채소", "축산/계란", "수산/건어물", "양념/조미료", "가공식품"};
	
	@GetMapping("/product")
	public String product(Model model) {
		model.addAttribute("category_arr", category_arr);
		return "/product/index";
	}
	
	@GetMapping("/product/index")
	public String productIndex(Model model) {
		model.addAttribute("category_arr", category_arr);
		return "/product/index";
	}
	
	@GetMapping("/product/list")
	public String productListPage(@RequestParam(value="p_category") String p_category, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="word", defaultValue="") String word, Model model) {
		int limit = 20;
		int totalCount = dao.productTotal(p_category, word);
		int totalPage = (int) Math.ceil((double)totalCount / limit);
		int startPage = ((page - 1) / 10) * 10 + 1;
		int endPage = startPage + 9;
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		
		model.addAttribute("list", dao.productList(p_category, page, limit, word));
		model.addAttribute("query", dao.productListQuery(p_category, page, limit, word));
		model.addAttribute("p_category", p_category);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("page", page);
		model.addAttribute("word", word);
		model.addAttribute("limit", limit);
		
		return "/product/list";
	}
	
	@GetMapping("/product/view")
	public String productViewPage(@RequestParam(value = "p_seq") int p_seq, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="word", defaultValue="1") String word, Model model) {
		model.addAttribute("productInfo", dao.productView(p_seq));
		model.addAttribute("page", page);
		model.addAttribute("word", word);
		
		return "/product/view";
	}
	
}
