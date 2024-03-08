package com.ex.springboot.controller.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.IfoodDAO;
import com.ex.springboot.interfaces.IproductDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class FoodController {

	@Autowired
	IfoodDAO dao;
	
	@Autowired
	IproductDAO daoproduct;
	
	@Autowired
	IboardDAO daoBoard;
	
	@GetMapping("/food/list")
	public String foodList(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "b_seq") int b_seq, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			
			int u_seq = userDTO.getU_seq();
			int limit = 1;
			int totalCount = dao.foodTotal(b_seq, u_seq);
			int totalPage = (int) Math.ceil((double)totalCount / limit);
			int startPage = ((page - 1) / 10) * 10 + 1;
			int endPage = startPage + 9;
			if(endPage > totalPage) {
				endPage = totalPage;
			}
			
			model.addAttribute("list", dao.foodList(page, b_seq, u_seq));
			model.addAttribute("totalCount", totalCount);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("page", page);
			model.addAttribute("limit", limit);
			model.addAttribute("b_seq", b_seq);
			model.addAttribute("boardInfo", daoBoard.boardView(b_seq));
			
			return "/food/list";
		}else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/food/view")
	public String foodView(@RequestParam(value = "f_code") String f_code, Model model) {
		
	    // 기존 코드
	    model.addAttribute("foodInfo", dao.foodView(f_code));
	    model.addAttribute("f_code", f_code);

	    // Recipe List 로직
	    ArrayList<String> recipeList = new ArrayList<>();
	    FoodDTO foodDTO = dao.foodView(f_code);
	    StringTokenizer token = new StringTokenizer(foodDTO.getF_recipe(), "|");
	    while(token.hasMoreElements()) {
	        recipeList.add((String) token.nextElement());
	    }
	    model.addAttribute("recipeList", recipeList);

	    
	    // f_code_arr 처리 로직
	    ArrayList<Map> ingredientList = new ArrayList<>();
	    
	    StringTokenizer tokenFCode = new StringTokenizer(foodDTO.getF_code_arr(), "|");
	    StringTokenizer tokenFVolume = new StringTokenizer(foodDTO.getF_volume_arr(), "|");
	    while(tokenFCode.hasMoreElements()) {
	    	Map<String, String> map = new HashMap<>();
	    	ProductDTO pInfo = daoproduct.productInfo((String)tokenFCode.nextElement());
	    	String fVolume = (String)tokenFVolume.nextElement();
	    	String pSeq = pInfo.getP_seq()+"";
	    	map.put("pSeq", pSeq);
	    	map.put("pName", pInfo.getP_name());
	    	map.put("fVolume", fVolume);
	    	ingredientList.add(map);
	    }
	    
	    System.out.println(ingredientList);
	    model.addAttribute("ingredientList", ingredientList);
	    
	    
	    
	    

	    return "/food/view";
	}
	
	
	@GetMapping("/food/search")
	public String foodListForName(@RequestParam(value="f_name") String f_name, Model model){
		ArrayList<FoodDTO> searchResult = dao.getFoodListForName(f_name);
		List<FoodDTO> randomSearchFoods = dao.getRandomSearchFoods();
		
		model.addAttribute("foodList", searchResult);
		model.addAttribute("randomSearchFoods", randomSearchFoods);
		model.addAttribute("f_name", f_name);
		
		return "/food/search";
	}
	
    

}
