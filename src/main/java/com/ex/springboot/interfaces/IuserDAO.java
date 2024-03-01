package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IuserDAO {
	UserDTO userDTO = new UserDTO();
	
	public UserDTO loginProcess(String u_id, String u_pw);
	public boolean joinProcess(UserDTO userDTO);
	public boolean updateUserInfoProcess(UserDTO userDTO);
	public UserDTO getUserInfo(int u_seq);
	public int deleteUser(int u_seq);
	public int isUserIdExist(String u_id);
	public int isUserNicknameExist(String u_nickname, int u_seq);
}