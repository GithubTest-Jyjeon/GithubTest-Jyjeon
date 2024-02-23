package com.ex.springboot.dao;

import com.ex.springboot.dto.UserDTO;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.SessionDTO;
import com.ex.springboot.interfaces.IsessionDAO;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class SessionDAO implements IsessionDAO {
	
	@Override
	public SessionDTO sessionInfo(HttpServletRequest request) {
		SessionDTO sessionDTO = new SessionDTO();
		
		return sessionDTO;
	}

	public String getSessionIdByUserId(String u_id) {
		// DB에서 userId로 sessionId 조회하는 로직 구현
		return "sessionId";
	}
	
}
