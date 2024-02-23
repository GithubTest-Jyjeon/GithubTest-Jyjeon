package com.ex.springboot.dao;

import com.ex.springboot.dto.UserDTO;
import org.apache.ibatis.annotations.Delete;
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
            user.setU_seq(rs.getInt("u_seq"));
            user.setU_id(rs.getString("u_id"));
            user.setU_pw(rs.getString("u_pw"));
            user.setU_name(rs.getString("u_name"));
            user.setU_nickname(rs.getString("u_nickname"));
            user.setU_email(rs.getString("u_email"));
            user.setU_birth(rs.getString("u_birth"));
            user.setU_postcode(rs.getString("u_postcode"));
            user.setU_address(rs.getString("u_address"));
            user.setU_address_detail(rs.getString("u_address_detail"));
            user.setU_address_extra(rs.getString("u_address_extra"));
            user.setU_gender(rs.getString("u_gender"));
            user.setU_phone(rs.getString("u_phone"));
            user.setU_reg_date(String.valueOf(rs.getTimestamp("u_reg_date"))); // 등록 일자
            user.setU_upd_date(String.valueOf(rs.getTimestamp("u_upd_date"))); // 수정 일자
            user.setU_log_date(String.valueOf(rs.getTimestamp("u_log_date"))); // 로그인 일자
            user.setU_del_date(String.valueOf(rs.getTimestamp("u_del_date"))); // 삭제 일자, 이 필드는 null일 수 있으므로, 데이터베이스에 따라 처리 방식을 고려해야 합니다.
            user.setU_del_yn(rs.getString("u_del_yn")); // 삭제 여부, 'N' 또는 null로 설정 가능

            System.out.println(user);
            return user;
        });
        return users.isEmpty() ? null : users.get(0);
    }

    public boolean updateUserInfoProcess(UserDTO user) {
        String sql = "UPDATE c##dbexam.cg_user SET u_pw=?, u_name=?, u_nickname=?, u_email=?, u_birth=?, u_postcode=?, u_address=?, u_address_detail=?, u_address_extra=?, u_gender=?, u_phone=? WHERE u_id=?";
        int result = jdbcTemplate.update(sql, user.getU_pw(), user.getU_name(), user.getU_nickname(), user.getU_email(), user.getU_birth(), user.getU_postcode(), user.getU_address(), user.getU_address_detail(), user.getU_address_extra(), user.getU_gender(), user.getU_phone(), user.getU_id());
        return result > 0;
    }

    public UserDTO getUserInfo(int u_seq) {
        String sql = "SELECT * FROM c##dbexam.cg_user WHERE u_seq = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, new Object[]{u_seq}, (rs, rowNum) -> {
            UserDTO user = new UserDTO();
            user.setU_seq(rs.getInt("u_seq"));
            user.setU_id(rs.getString("u_id"));
            user.setU_pw(rs.getString("u_pw"));
            user.setU_name(rs.getString("u_name"));
            user.setU_nickname(rs.getString("u_nickname"));
            user.setU_email(rs.getString("u_email"));
            user.setU_birth(rs.getString("u_birth"));
            user.setU_postcode(rs.getString("u_postcode"));
            user.setU_address(rs.getString("u_address"));
            user.setU_address_detail(rs.getString("u_address_detail"));
            user.setU_address_extra(rs.getString("u_address_extra"));
            user.setU_gender(rs.getString("u_gender"));
            user.setU_phone(rs.getString("u_phone"));
            // 필요한 나머지 필드도 설정
            return user;
        });
        return users.isEmpty() ? null : users.get(0);
    }

    public boolean deleteUser(int u_seq) {
        String sql = "DELETE FROM c##dbexam.cg_user WHERE u_seq = ?";
        int result = jdbcTemplate.update(sql, u_seq);
        return result > 0;
    }
}
