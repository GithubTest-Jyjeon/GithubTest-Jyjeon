package com.ex.springboot.controller.food;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.ReplyDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IboardDAO;
import com.ex.springboot.interfaces.IfoodDAO;
import com.ex.springboot.interfaces.IheartDAO;
import com.ex.springboot.interfaces.IproductDAO;
import com.ex.springboot.interfaces.IreplyDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class FoodController {

	@Autowired
	IfoodDAO dao;
	
	@Autowired
	IproductDAO daoProduct;
	
	@Autowired
	IboardDAO daoBoard;
	
	@Autowired
	IheartDAO daoHeart;
	
	@Autowired
	IreplyDAO daoReply;
	
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
			
			return "food/list";
		}else {
			return "redirect:/user/login";
		}
	}
	
	@GetMapping("/food/view")
	public String foodView(@RequestParam(value = "f_code") String f_code, Model model) {
	    // 기존 코드
		FoodDTO foodInfo = dao.foodView(f_code);
	    model.addAttribute("foodInfo", foodInfo);
	    model.addAttribute("f_code", f_code);

	    // Recipe List 로직
	    ArrayList<String> recipeList = new ArrayList<>();
	    FoodDTO foodDTO = dao.foodView(f_code);
	    ArrayList<ReplyDTO> replyList = daoReply.getReplyList(foodDTO.getF_seq());
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
	    	ProductDTO pInfo = daoProduct.productInfo((String)tokenFCode.nextElement());
	    	String fVolume = (String)tokenFVolume.nextElement();
	    	String pSeq = pInfo.getP_seq()+"";
	    	map.put("pSeq", pSeq);
	    	map.put("pName", pInfo.getP_name());
	    	map.put("fVolume", fVolume);
	    	ingredientList.add(map);
	    }
	    
	    model.addAttribute("replyList", replyList);
	    model.addAttribute("ingredientList", ingredientList);
	    return "food/view";
	}
	
	@GetMapping("/food/write")
	public String foodWrite(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			return "food/write";
		}else {
			return "user/login";
		}
	}
	
	@GetMapping("/food/update")
	public String foodUpdate(HttpServletRequest request, @RequestParam(value="f_code") String f_code, Model model) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			if(userDTO.getU_seq() == 0) {
				return "food/view?f_code="+f_code;
			}else {
				FoodDTO foodInfo = new FoodDTO();
				foodInfo = dao.foodView(f_code);
				String codeString = foodInfo.getF_code_arr();
				String volumeString = foodInfo.getF_volume_arr();
				String recipeString = foodInfo.getF_recipe();
				
				List<String> pCodeList = new ArrayList<>();
				List<String> pNameList = new ArrayList<>();
				List<String> fVolumeList = new ArrayList<>();
				List<String> recipeList = new ArrayList<>();

				int cnt = 0;
				StringTokenizer codeToken = new StringTokenizer(codeString, "|");
				StringTokenizer volumeToken = new StringTokenizer(volumeString, "|");
				while(codeToken.hasMoreElements()) {
					cnt++;
					
					String p_code = (String) codeToken.nextElement();
					String p_name = daoProduct.productInfo(p_code).getP_name();
					String f_volume = (String) volumeToken.nextElement();
					
					pCodeList.add(p_code);
					pNameList.add(p_name);
					fVolumeList.add(f_volume);
				}
				
				StringTokenizer recipeToken = new StringTokenizer(recipeString, "|");
				while(recipeToken.hasMoreElements()) {
					recipeList.add((String) recipeToken.nextElement());
				}
				
				model.addAttribute("productCnt", cnt);
				model.addAttribute("foodInfo", foodInfo);
				model.addAttribute("pCodeList", pCodeList);
				model.addAttribute("pNameList", pNameList);
				model.addAttribute("fVolumeList", fVolumeList);
				model.addAttribute("recipeList", recipeList);
				
				return "food/update";
			}
		}else {
			return "user/login";
		}
	}
	
	@ResponseBody
	@PostMapping("/food/writeProcess")
	public String foodWriteProcess(HttpServletRequest request, @RequestParam("f_image") MultipartFile file, Model model) throws IOException {
		String UPLOAD_DIRECTORY = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
		
		try {
			StringBuilder fileNames = new StringBuilder();
			String originalFilename = file.getOriginalFilename();
			long timestamp = System.currentTimeMillis();
			String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			String newFilename = timestamp+"."+ext;
			Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, newFilename);
			fileNames.append(newFilename);
			byte[] fileSize = file.getBytes();
			Files.write(fileNameAndPath, fileSize);
			
			String f_name = request.getParameter("f_name");
			String f_code_arr = request.getParameter("f_code_arr");
			String f_volume_arr = request.getParameter("f_volume_arr");
			String f_recipe = request.getParameter("f_recipe");
			String f_image = "/uploads/"+newFilename;
			String f_type_theme = request.getParameter("f_type_theme");
			String f_type_main = request.getParameter("f_type_main");
			String f_type_soup = request.getParameter("f_type_soup");
			String f_type_spicy = request.getParameter("f_type_spicy");
			
			FoodDTO foodDTO = new FoodDTO();
			foodDTO.setF_name(f_name);
			foodDTO.setF_code_arr(f_code_arr);
			foodDTO.setF_volume_arr(f_volume_arr);
			foodDTO.setF_recipe(f_recipe);
			foodDTO.setF_image(f_image);
			foodDTO.setF_type_theme(f_type_theme);
			foodDTO.setF_type_main(f_type_main);
			foodDTO.setF_type_soup(f_type_soup);
			foodDTO.setF_type_spicy(f_type_spicy);
			
			HttpSession session = request.getSession();
			UserDTO userDTO = (UserDTO) session.getAttribute("userSession");

			if(userDTO != null) {
				int u_seq = userDTO.getU_seq();
				String u_nickname = userDTO.getU_nickname();
				String f_code = dao.foodWrite(foodDTO, u_seq, u_nickname);
				
				model.addAttribute("f_code", f_code);
				
				return f_code;
			}else {
				return "errorUserDTO";
			}
		} catch(Exception e) {
			e.getStackTrace();
			return "errorInsert";
		}
	}
	
	
	@ResponseBody
	@PostMapping("/food/updateProcess")
	public String foodUpdateProcess(HttpServletRequest request, @RequestParam(required = false, value="f_image") MultipartFile file, Model model) throws IOException {
		
		String UPLOAD_DIRECTORY = System.getProperty("user.dir")+"/src/main/resources/static";
		
		try {
			StringBuilder fileNames = new StringBuilder();
			String newFilename = null;
			Path fileNameAndPath = null;
			byte[] fileSize = null;
			
			String f_image_final = "";
			
			if(file.getSize() == 0) {
				f_image_final = request.getParameter("f_image_original");
			}else {
				String originalFilename = file.getOriginalFilename();
				long timestamp = System.currentTimeMillis();
				String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
				newFilename = timestamp+"."+ext;
				fileNameAndPath = Paths.get(UPLOAD_DIRECTORY+"/uploads", newFilename);
				fileNames.append(newFilename);
				fileSize = file.getBytes();
				Files.write(fileNameAndPath, fileSize);
				
				Path deletePath = Paths.get(UPLOAD_DIRECTORY, request.getParameter("f_image_original"));
				Files.delete(deletePath);
				f_image_final = "/uploads/"+newFilename;
			}
			
			String f_code = request.getParameter("f_code");
			String f_name = request.getParameter("f_name");
			String f_code_arr = request.getParameter("f_code_arr");
			String f_volume_arr = request.getParameter("f_volume_arr");
			String f_recipe = request.getParameter("f_recipe");
			String f_image = f_image_final;
			String f_type_theme = request.getParameter("f_type_theme");
			String f_type_main = request.getParameter("f_type_main");
			String f_type_soup = request.getParameter("f_type_soup");
			String f_type_spicy = request.getParameter("f_type_spicy");
			
			FoodDTO foodDTO = new FoodDTO();
			foodDTO.setF_code(f_code);
			foodDTO.setF_name(f_name);
			foodDTO.setF_code_arr(f_code_arr);
			foodDTO.setF_volume_arr(f_volume_arr);
			foodDTO.setF_recipe(f_recipe);
			foodDTO.setF_image(f_image);
			foodDTO.setF_type_theme(f_type_theme);
			foodDTO.setF_type_main(f_type_main);
			foodDTO.setF_type_soup(f_type_soup);
			foodDTO.setF_type_spicy(f_type_spicy);
			
			HttpSession session = request.getSession();
			UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
			
			if(userDTO != null) {
				int u_seq = userDTO.getU_seq();
				f_code = dao.foodUpdate(foodDTO, u_seq);

				model.addAttribute("f_code", f_code);
				
				return f_code;
			}else {
				System.out.println("errorUserDTO");
				Files.delete(fileNameAndPath);
				return "errorUserDTO";
			}
		} catch(Exception e) {
			e.getStackTrace();
			return "errorUpdate";
		}
	}
	
	
	
	@GetMapping("/food/search")
	public String foodListForName(@RequestParam(value="f_name") String f_name, Model model){
		String f_name_replace = f_name.replace(" ", "");
		ArrayList<FoodDTO> searchResult = dao.getFoodListForName(f_name_replace);
		List<FoodDTO> randomSearchFoods = dao.getRandomSearchFoods();
		
		model.addAttribute("foodList", searchResult);
		model.addAttribute("randomSearchFoods", randomSearchFoods);
		model.addAttribute("f_name", f_name);
		
		return "food/search";
	}
	
	
	@GetMapping("/food/heartCheck")
	public @ResponseBody int foodHeartCheck(@RequestParam(value="f_seq") int f_seq, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			int u_seq = userDTO.getU_seq();
			
			return dao.foodHeartCheck(f_seq, u_seq);
		}else {
			return 0;
		}
	}
	
    
	@PostMapping("/food/heartOn")
	public @ResponseBody int foodHeartOn(@RequestParam(value="f_seq") int f_seq, HttpServletRequest request){
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			int u_seq = userDTO.getU_seq();
		
			return dao.foodHeartOn(f_seq, u_seq);
		}else {
			return 0;
		}
	}
	
	@PostMapping("/food/heartOff")
	public @ResponseBody int foodHeartOff(@RequestParam(value="f_seq") int f_seq, HttpServletRequest request){
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			int u_seq = userDTO.getU_seq();
		
			return dao.foodHeartOff(f_seq, u_seq);
		}else {
			return 0;
		}
	}
	
	@GetMapping("/food/heartCount")
	public @ResponseBody int foodHeartCount(@RequestParam(value="f_seq") int f_seq) {
		return daoHeart.getHeartCount(f_seq);
	}
	
	@GetMapping("/food/replyCount")
	public @ResponseBody int foodReplyCount(@RequestParam(value="f_seq") int f_seq) {
		return daoReply.getReplyCount(f_seq);
	}
	
	@PostMapping("/food/replyInsert")
	public @ResponseBody int replyInsertProcess(@RequestParam(value="f_seq") int f_seq, @RequestParam(value="fr_comment") String fr_comment, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO) session.getAttribute("userSession");
		
		if(userDTO != null) {
			int u_seq = userDTO.getU_seq();
			String u_nickname = userDTO.getU_nickname();
			daoReply.replyInsertProcess(f_seq, u_seq, u_nickname, fr_comment);
			return 1;
		}else {
			return 0;
		}
	}

}
