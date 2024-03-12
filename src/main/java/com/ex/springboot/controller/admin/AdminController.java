package com.ex.springboot.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ex.springboot.dao.FoodDAO;
import com.ex.springboot.dao.ProductDAO;
import com.ex.springboot.dto.ProductDTO;
import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IuserDAO;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    IuserDAO userDao;   
    @Autowired
    FoodDAO foodDao;
    @Autowired
    ProductDAO productDAO;
    
    
    @GetMapping
    public String dashboard() {
        return "admin/dashboard"; // dashboard.html 뷰를 반환
    }

    @GetMapping("/products")
    public String productList(Model model) {
        List<ProductDTO> productList = productDAO.getAllProducts();
        model.addAttribute("products", productList);
        return "admin/products"; // Thymeleaf를 이용한 상품 리스트 페이지 경로
    }
    
    @GetMapping("/editProduct/{p_seq}")
    public String editProduct(@PathVariable("p_seq") int p_seq, Model model) {
        ProductDTO product = productDAO.productView(p_seq);
        model.addAttribute("product", product);
        return "admin/editProduct"; // 상품 수정 페이지
    }

    @GetMapping("/deleteProduct/{p_seq}")
    public String deleteProduct(@PathVariable("p_seq") int p_seq, RedirectAttributes redirectAttributes) {
        int rowsAffected = productDAO.deleteProduct(p_seq); // 실제 상품 삭제 호출
        if (rowsAffected > 0) {
            redirectAttributes.addFlashAttribute("message", "상품이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "상품 삭제 실패. 상품을 찾을 수 없습니다.");
        }
        return "redirect:/admin/products";
    }
    
    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute ProductDTO product, RedirectAttributes redirectAttributes) {
        int rowsAffected = productDAO.updateProduct(product);
        if (rowsAffected > 0) {
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 수정되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "상품 수정 실패.");
        }
        return "redirect:/admin/products";


    
    }
    
    
    @GetMapping("/addProduct")
    public String addProductForm() {
        return "admin/addProduct"; // 상품 추가 폼 페이지 반환
    }

    @PostMapping("/insertProduct")
    public String insertProduct(ProductDTO product, RedirectAttributes redirectAttributes) {
        // ProductDAO를 사용하여 데이터베이스에 상품 정보 추가
        int rowsAffected = productDAO.insertProduct(product);
        if (rowsAffected > 0) {
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 추가되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "상품 추가 실패.");
        }
        return "redirect:/admin/products"; // 상품 리스트 페이지로 리다이렉트
    }
    
    
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> userList = userDao.getAllUsers(); // 수정된 getAllUsers() 호출
        model.addAttribute("users", userList);
        return "admin/users"; // 사용자 목록 페이지
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = userDao.deleteUser(u_seq); // 삭제 또는 탈퇴 처리
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제(또는 탈퇴 처리) 성공");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제(또는 탈퇴 처리) 실패");
        }
        return "redirect:/admin/users";
    }
    
    @GetMapping("/deleteUserCompletely")
    public String deleteUserCompletely(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = userDao.deleteUserCompletely(u_seq);
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자가 완전히 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 삭제 실패.");
        }
        return "redirect:/admin/users";
    }
    
    @GetMapping("/reactivateUser")
    public String reactivateUser(@RequestParam("u_seq") int u_seq, RedirectAttributes redirectAttributes) {
        int result = userDao.reactivateUser(u_seq);
        if(result > 0) {
            redirectAttributes.addFlashAttribute("message", "사용자가 성공적으로 재활성화되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자 재활성화 실패.");
        }
        return "redirect:/admin/users";
    }



    
    


}
