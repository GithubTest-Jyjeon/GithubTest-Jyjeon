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
	public boolean joinProcess(UserDTO user) {
        String sql = "insert into cg_user (u_seq, u_id, u_pw, u_name, u_nickname, u_email, u_birth, u_postcode, u_address, u_address_detail, u_address_extra, u_gender, u_phone, u_reg_date, u_upd_date, u_log_date, u_del_date, u_del_yn) VALUES (u_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, sysdate, sysdate, null, 'N')";

        int result = template.update(sql, user.getU_id(), user.getU_pw(), user.getU_name(), user.getU_nickname(), user.getU_email(), user.getU_birth(), user.getU_postcode(), user.getU_address(), user.getU_address_detail(), user.getU_address_extra(), user.getU_gender(), user.getU_phone());

        return result > 0;
    }

	@Override
    public UserDTO loginProcess(String u_id, String u_pw) {
        try {
        	String sql = "select * from cg_user where u_id = '"+u_id+"' and u_pw = '"+u_pw+"'";
        	return template.queryForObject(sql, new BeanPropertyRowMapper<UserDTO>(UserDTO.class));
        } catch (IncorrectResultSizeDataAccessException error) {
        	return null;
        }
        
    }

	@Override
	public boolean updateUserInfoProcess(UserDTO user) {
        String sql = "UPDATE cg_user SET u_pw=?, u_name=?, u_nickname=?, u_email=?, u_birth=?, u_postcode=?, u_address=?, u_address_detail=?, u_address_extra=?, u_gender=?, u_phone=? WHERE u_id=?";
        int result = template.update(sql, user.getU_pw(), user.getU_name(), user.getU_nickname(), user.getU_email(), user.getU_birth(), user.getU_postcode(), user.getU_address(), user.getU_address_detail(), user.getU_address_extra(), user.getU_gender(), user.getU_phone(), user.getU_id());
        return result > 0;
    }
	

}
