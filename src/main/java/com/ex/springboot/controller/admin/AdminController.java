package com.ex.springboot.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ex.springboot.dao.FoodDAO;
import com.ex.springboot.dao.ProductDAO;
import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.FoodSetDTO;
import com.ex.springboot.interfaces.IuserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class AdminController {

    @Autowired
    IuserDAO dao;   
    @Autowired
    FoodDAO foodDao;
    @Autowired
   	ProductDAO productDAO;


   
   

   
    
    
}
