package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.UserDTO;

@Mapper
public interface IuserDAO {
	UserDTO userDTO = new UserDTO();
	
	public UserDTO loginProcess(String u_id, String u_pw);
	public boolean joinProcess(UserDTO userDTO);
	public boolean updateUserInfoProcess(UserDTO userDTO);
	public void nicknameChange(int u_seq, String u_nickname);
	public UserDTO getUserInfo(int u_seq);
	public int deleteUser(int u_seq);
	public int isUserIdExist(String u_id);
	public int isUserNicknameExist(String u_nickname, int u_seq);
	public int countUserIdByEmail(String u_email);
	public UserDTO findUserByEmail(String u_email);
	public int countUserPwById(String u_id, String u_email);
	public void resetUserPw(String u_id, String u_pw, String u_email);
}