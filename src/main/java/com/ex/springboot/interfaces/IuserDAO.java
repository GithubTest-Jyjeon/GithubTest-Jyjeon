package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;

@Mapper
public interface IuserDAO {
	UserDTO userDTO = new UserDTO();
	
	public UserDTO loginProcess(String u_id, String u_pw);
	public boolean joinProcess(UserDTO userDTO);
	public boolean updateUserInfoProcess(UserDTO userDTO);
}