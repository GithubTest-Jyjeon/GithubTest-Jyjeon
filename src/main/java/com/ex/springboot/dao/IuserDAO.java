package com.ex.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.userDTO;

@Mapper
public interface IuserDAO {
//	public List<SimpleBbsDTO> listDao();
//	public SimpleBbsDTO viewDao(String id);
//	public int writeDao(String writer, String title, String content);
//	public int deleteDao(String id);
	
	public List<userDTO> loginDAO(String u_id, String u_pw);
	public int joinDAO(String u_id, String u_pw, String u_name);
}