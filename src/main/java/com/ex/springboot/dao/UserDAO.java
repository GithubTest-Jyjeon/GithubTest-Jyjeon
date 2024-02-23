package com.ex.springboot.dao;

import com.ex.springboot.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class UserDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean joinProcess(UserDTO user) {
        String sql = "insert into c##dbexam.cg_user (u_seq, u_id, u_pw, u_name, u_nickname, u_email, u_birth, u_postcode, u_address, u_address_detail, u_address_extra, u_gender, u_phone, u_reg_date, u_upd_date, u_log_date, u_del_date, u_del_yn) VALUES (u_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, sysdate, sysdate, null, 'N')";

        int result = jdbcTemplate.update(sql, user.getU_id(), user.getU_pw(), user.getU_name(), user.getU_nickname(), user.getU_email(), user.getU_birth(), user.getU_postcode(), user.getU_address(), user.getU_address_detail(), user.getU_address_extra(), user.getU_gender(), user.getU_phone());

        return result > 0;
    }

    public UserDTO loginProcess(String u_id, String u_pw) {
        String sql = "select * from c##dbexam.cg_user where u_id = ? and u_pw = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, new Object[]{u_id, u_pw}, (rs, rowNum) -> {
            UserDTO user = new UserDTO();
            // UserDTO 필드 설정
            user.setU_id(rs.getString("u_id"));
            // 필요한 나머지 필드도 설정
            return user;
        });
        return users.isEmpty() ? null : users.get(0);
    }

    public boolean updateUserInfoProcess(UserDTO user) {
        String sql = "UPDATE c##dbexam.cg_user SET u_pw=?, u_name=?, u_nickname=?, u_email=?, u_birth=?, u_postcode=?, u_address=?, u_address_detail=?, u_address_extra=?, u_gender=?, u_phone=? WHERE u_id=?";
        int result = jdbcTemplate.update(sql, user.getU_pw(), user.getU_name(), user.getU_nickname(), user.getU_email(), user.getU_birth(), user.getU_postcode(), user.getU_address(), user.getU_address_detail(), user.getU_address_extra(), user.getU_gender(), user.getU_phone(), user.getU_id());
        return result > 0;
    }
}
