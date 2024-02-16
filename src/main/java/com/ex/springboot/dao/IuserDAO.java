package com.ex.springboot.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IuserDAO {
	UserDTO userDTO = new UserDTO();
	
	public List<UserDTO> loginDAO(String u_id, String u_pw);
	public boolean joinDAO(UserDTO userDTO);
	public List<UserDTO> myInfoDAO(int u_seq);
}