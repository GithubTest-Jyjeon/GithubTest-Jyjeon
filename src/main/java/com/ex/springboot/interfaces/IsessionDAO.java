package com.ex.springboot.interfaces;

import com.ex.springboot.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.SessionDTO;

import jakarta.servlet.http.HttpServletRequest;

@Mapper
public interface IsessionDAO {
	SessionDTO sessionDTO = new SessionDTO();
	
	public SessionDTO sessionInfo(HttpServletRequest request);
}