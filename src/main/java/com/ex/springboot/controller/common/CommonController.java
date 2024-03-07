package com.ex.springboot.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ex.springboot.dao.FoodDAO;
import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class CommonController {

    @Autowired
    IuserDAO dao;
    
    @Autowired
    FoodDAO foodDao;
    
    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userSession") != null);
        model.addAttribute("isLoggedIn", isLoggedIn ? "true" : "false"); // 로그인 상태를 문자열로 모델에 추가

        // 1. 랜덤한 8개의 데이터
        List<FoodDTO> randomFoods = foodDao.getRandomFoods();
        model.addAttribute("randomFoods", randomFoods);

        // 2. f_type_theme가 k, u, c, j, d인 데이터 랜덤하게 8개씩 따로 출력
        String[] themes = {"K", "U", "C", "J", "D"};
        for (String theme : themes) {
            List<FoodDTO> foodsByTheme = foodDao.getFoodsByTheme(theme);
            // 모델에 테마별로 별도의 속성으로 추가
            model.addAttribute(theme + "Foods", foodsByTheme);
        }

        // 3. 전체 cg_food의 정보 출력
        List<FoodDTO> allFoods = foodDao.getAllFoods();
        model.addAttribute("allFoods", allFoods);

        return "/common/main";
    }

   
    
    
}
