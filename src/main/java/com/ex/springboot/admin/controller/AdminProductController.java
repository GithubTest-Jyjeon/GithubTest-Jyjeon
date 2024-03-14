package com.ex.springboot.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ex.springboot.admin.dto.AdminProductDTO;
import com.ex.springboot.admin.interfaces.IadminProductDAO;

@Controller
public class AdminProductController {

	final String PAGE = "1";
	final int LIMIT = 12;
	
	@Autowired
	IadminProductDAO dao;
	
	@GetMapping("/admin/product/list")
    public String productList(
		@RequestParam(value="page", defaultValue=PAGE) int page,
		@RequestParam(value="category", defaultValue="") String category,
		@RequestParam(value="p_name", defaultValue="") String p_name,
		Model model
	) {	
		System.out.println("category : "+category);
		int totalCount = dao.getProductCount(category);
		int totalPage = (int) Math.ceil((double)totalCount / LIMIT);
		int startPage = ((page - 1) / 10) * 10 + 1;
		int endPage = startPage + 9;
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		
        List<AdminProductDTO> productList = dao.getProductList(page, LIMIT, category, p_name);
        
        model.addAttribute("page", page);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("category", category);
        model.addAttribute("p_name", p_name);
        model.addAttribute("productList", productList);
        
        return "/admin/productList";
    }
    
    @GetMapping("/admin/product/view")
    public String productView(
		@RequestParam(value="page", defaultValue=PAGE) int page, 
		@RequestParam(value="category", defaultValue="") String category,
		@RequestParam(value="p_seq", defaultValue="0") int p_seq,
		Model model
	) {
        AdminProductDTO productInfo = dao.getProductInfo(p_seq);
        
        model.addAttribute("page", page);
        model.addAttribute("category", category);
        model.addAttribute("productInfo", productInfo);
        
        return "/admin/productView";
    }
    
    @GetMapping("/admin/product/insert")
    public String productInsert(
		@RequestParam(value="page", defaultValue=PAGE) int page, 
		@RequestParam(value="category", defaultValue="") String category,
		Model model
	) { 
        model.addAttribute("page", page);
        model.addAttribute("category", category);
        
        return "/admin/productInsert";
    }
    
    @PostMapping("/admin/product/insertProcess")
    public String productInsertProcess(
    	AdminProductDTO dto,
		@RequestParam(value="page", defaultValue=PAGE) int page,
		@RequestParam(value="category", defaultValue="") String category,
		RedirectAttributes re
	) {
    	re.addAttribute("page", page);
    	
    	if(category.length() > 0) {
    		re.addAttribute("category", category);
        }else {
        	re.addAttribute("category", dto.getP_category());
        }
    	
        if(dto.getP_dc_yn().equals("N")) {
        	dto.setP_dc_percent(0);
        }
        
        int p_seq = dao.getLastProductSeq() + 1;
        String p_code = dao.getLastProductCode(dto.getP_category());
        dto.setP_code(String.format("%04d", (Integer.parseInt(p_code) + 1)));
        
        int result = dao.setProductInfo(dto);

        re.addAttribute("p_seq", p_seq);
        
        if (result > 0) {
            return "redirect:/admin/product/view";
        } else {            
            return "redirect:/admin/product/insert";
        }
    }

    @GetMapping("/admin/product/update")
    public String productUpdate(
		@RequestParam(value="page", defaultValue=PAGE) int page,
		@RequestParam(value="category", defaultValue="") String category,
		@RequestParam(value="p_seq", defaultValue="") int p_seq,
		Model model
	) {
		AdminProductDTO dto = new AdminProductDTO();
		dto = dao.getProductInfo(p_seq);
		
		model.addAttribute("page", page);
        
        if(category.length() > 0) {
        	model.addAttribute("category", category);
        }else {
        	model.addAttribute("category", dto.getP_category());
        }
        
        if (dto != null) {
        	model.addAttribute("productInfo", dto);
            return "/admin/productUpdate";
        } else {
            return "redirect:/admin/product/list";
        }
    }
    
    @PostMapping("/admin/product/updateProcess")
    public String productUpdateProcess(
		AdminProductDTO dto,
		@RequestParam(value="page", defaultValue=PAGE) int page,
		@RequestParam(value="category", defaultValue="") String category,
		@RequestParam(value="p_seq", defaultValue="") int p_seq,
		RedirectAttributes re
	) {
    	re.addAttribute("page", page);
    	
    	if(category.length() > 0) {
    		re.addAttribute("category", category);
        }else {
        	re.addAttribute("category", dto.getP_category());
        }
    	
    	re.addAttribute("p_seq", p_seq);
        
        if(dto.getP_dc_yn().equals("N")) {
        	dto.setP_dc_percent(0);
        }
        
        int result = dao.updProductInfo(dto);

        if (result > 0) {
            return "redirect:/admin/product/view";
        } else {            
            return "redirect:/admin/product/update";
        }
    }

    @GetMapping("/admin/product/delete")
    public String productDelete(
		@RequestParam(value="page", defaultValue=PAGE) int page,
		@RequestParam(value="category", defaultValue="") String category,
		@RequestParam(value="p_seq", defaultValue="0") int p_seq,
		Model model
    ) {
		model.addAttribute("page", page);
        model.addAttribute("limit", LIMIT);
        model.addAttribute("category", category);
		
        int result = dao.delProductInfo(p_seq); // 실제 상품 삭제 호출
        if (result > 0) {
            model.addAttribute("message", "상품이 삭제되었습니다.");
            
            return "redirect:/admin/product/list";
        } else {
            model.addAttribute("message", "상품 삭제 실패. 상품을 찾을 수 없습니다.");
            model.addAttribute("p_seq", p_seq);
            
            return "redirect:/admin/product/view";
        }
    }
    
    
    @GetMapping("/addProduct")
    public String addProductForm() {
        return "admin/addProduct"; // 상품 추가 폼 페이지 반환
    }

    

}
