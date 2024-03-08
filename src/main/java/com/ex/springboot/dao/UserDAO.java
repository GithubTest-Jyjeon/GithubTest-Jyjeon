package com.ex.springboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.UserDTO;
import com.ex.springboot.interfaces.IuserDAO;

@Primary
@Repository
public class UserDAO implements IuserDAO {

	@Autowired
	JdbcTemplate template;

	@Override
	public boolean joinProcess(UserDTO userDTO) {
        String sql = "insert into cg_user ("
        		+ "u_seq,"
        		+ "u_id,"
        		+ "u_pw,"
        		+ "u_name,"
        		+ "u_nickname,"
        		+ "u_email,"
        		+ "u_birth,"
        		+ "u_postcode,"
        		+ "u_address,"
        		+ "u_address_detail,"
        		+ "u_address_extra,"
        		+ "u_gender,"
        		+ "u_phone,"
        		+ "u_reg_date, u_upd_date, u_log_date, u_del_date, u_del_yn) VALUES (USER_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, sysdate, sysdate, '', 'N')";

        int result = template.update(sql, userDTO.getU_id(), userDTO.getU_pw(), userDTO.getU_name(), userDTO.getU_nickname(), userDTO.getU_email(), userDTO.getU_birth(), userDTO.getU_postcode(), userDTO.getU_address(), userDTO.getU_address_detail(), userDTO.getU_address_extra(), userDTO.getU_gender(), userDTO.getU_phone());

        return result > 0;
    }

	@Override
    public UserDTO loginProcess(String u_id, String u_pw) {
        try {
        	String sql = "select * from cg_user where u_id = '"+u_id+"' and u_pw = '"+u_pw+"' and u_del_yn = 'N'";
        	UserDTO result = template.queryForObject(sql, new BeanPropertyRowMapper<UserDTO>(UserDTO.class));
        	int u_seq = result.getU_seq();
        	if(result != null) {
        		String updateLoginDate = "update cg_user set u_log_date=sysdate where u_seq = ?";
        		template.update(updateLoginDate, u_seq);
        	}
        	return result;
        } catch (IncorrectResultSizeDataAccessException error) {
        	return null;
        }
        
    }

	@Override
	public boolean updateUserInfoProcess(UserDTO userDTO) {
        String sql = "UPDATE cg_user SET u_pw=?, u_name=?, u_nickname=?, u_email=?, u_birth=?, u_postcode=?, u_address=?, u_address_detail=?, u_address_extra=?, u_gender=?, u_phone=?, u_upd_date=sysdate WHERE u_seq=?";
        int result = template.update(sql, userDTO.getU_pw(), userDTO.getU_name(), userDTO.getU_nickname(), userDTO.getU_email(), userDTO.getU_birth(), userDTO.getU_postcode(), userDTO.getU_address(), userDTO.getU_address_detail(), userDTO.getU_address_extra(), userDTO.getU_gender(), userDTO.getU_phone(), userDTO.getU_seq());
        return result > 0;
    }
	
	@Override
	public void nicknameChange(int u_seq, String u_nickname) {
		String updateQuery = "update cg_board set u_nickname = '"+u_nickname+"' where u_seq = "+u_seq;
		System.out.println(updateQuery);
		template.update(updateQuery);
	}
	
	@Override
	public UserDTO getUserInfo(int u_seq) {
		String selectQuery = "select * from cg_user where u_seq = "+u_seq;
		
		return (UserDTO) template.queryForObject(selectQuery, new BeanPropertyRowMapper<UserDTO>(UserDTO.class)); 
	}
	
	@Override
	public int deleteUser(int u_seq) {
        String deleteQuery = "update cg_user set u_del_yn = 'Y', u_del_date = sysdate where u_seq = "+u_seq;
        
        return template.update(deleteQuery);
    }

	@Override
    public int isUserIdExist(String u_id) {
        String sql = "select count(*) from cg_user where u_id = '"+u_id+"'";
        int count = template.queryForObject(sql, Integer.class);
        
        return count;
    }

	@Override
    public int isUserNicknameExist(String u_nickname, int u_seq) {
		String sql = "";
		if(u_seq > 0) {
			sql = "select count(*) from cg_user where u_nickname = '"+u_nickname+"' and u_seq != "+u_seq;
		}else {
			sql = "select count(*) from cg_user where u_nickname = '"+u_nickname+"'";
		}
        
        int count = template.queryForObject(sql, Integer.class);
        
        return count;
    }

}
