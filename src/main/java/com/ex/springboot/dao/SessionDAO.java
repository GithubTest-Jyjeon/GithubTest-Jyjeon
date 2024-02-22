package com.ex.springboot.dao;

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

	
}
