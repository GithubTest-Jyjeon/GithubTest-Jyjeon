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

import org.json.simple.JSONObject;
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
	IproductDAO daoproduct;
	
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
	    	ProductDTO pInfo = daoproduct.productInfo((String)tokenFCode.nextElement());
	    	String fVolume = (String)tokenFVolume.nextElement();
	    	String pSeq = pInfo.getP_seq()+"";
	    	map.put("pSeq", pSeq);
	    	map.put("pName", pInfo.getP_name());
	    	map.put("fVolume", fVolume);
	    	ingredientList.add(map);
	    }
	    
	    model.addAttribute("replyList", replyList);
	    model.addAttribute("ingredientList", ingredientList);

	    return "/food/view";
	}
	
	@GetMapping("/food/write")
	public String foodWrite() {
		return "/food/write";
	}
	
	
	@PostMapping("/food/writeProcess")
	public String foodWriteProcess(Model model, MultipartFile file, Object msg) throws IOException {
		
		String UPLOAD_DIRECTORY = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
		
		JSONObject obj = new JSONObject();
		
		try {
			StringBuilder fileNames = new StringBuilder();
			Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
			fileNames.append(file.getOriginalFilename());
			byte[] fileSize = file.getBytes();
			Files.write(fileNameAndPath, fileSize);
			
			System.out.println("파일이 저장되는 경로 : "+fileNameAndPath+"\n");
			System.out.println("fileNames 에서 얻은 이미지 파일명 : "+fileNames+"\n");
			System.out.println("model 에 저장한 메세지 : "+msg+"\n");
			System.out.println("--------------------------------------------------");
			
			obj.put("success", true);
			obj.put("업로드 결과", "성공");
			obj.put("파일 저장명", fileNameAndPath);
			
			model.addAttribute("fileNameAndPath", fileNameAndPath);
			model.addAttribute("fileNames", fileNames);
			model.addAttribute("filePath", UPLOAD_DIRECTORY);
			
			String[] contentType = file.getContentType().split("/");			
			model.addAttribute("contentType", contentType[0]);
		} catch(Exception e) {
			e.getStackTrace();
			obj.put("success", false);
			obj.put("업로드 결과", "실패");
		}
		
		return "/food/write";
		// return obj.toJSONString();
	}
	
	
	@GetMapping("/food/search")
	public String foodListForName(@RequestParam(value="f_name") String f_name, Model model){
		String f_name_replace = f_name.replace(" ", "");
		ArrayList<FoodDTO> searchResult = dao.getFoodListForName(f_name_replace);
		List<FoodDTO> randomSearchFoods = dao.getRandomSearchFoods();
		
		model.addAttribute("foodList", searchResult);
		model.addAttribute("randomSearchFoods", randomSearchFoods);
		model.addAttribute("f_name", f_name);
		
		return "/food/search";
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
