package com.ex.springboot.admin.interfaces;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.ex.springboot.admin.dto.AdminUserDTO; // 수정: UserDTO 대신 AdminUserDTO를 사용

@Mapper
public interface IadminUserDAO {
    // UserDTO를 AdminUserDTO로 변경
    public AdminUserDTO loginProcess(String u_id, String u_pw);
    public boolean joinProcess(AdminUserDTO userDTO);
    public boolean updateUserInfoProcess(AdminUserDTO userDTO);
    public void nicknameChange(int u_seq, String u_nickname);
    public AdminUserDTO getUserInfo(int u_seq);
    public int deleteUser(int u_seq);
    public int isUserIdExist(String u_id);
    public int isUserNicknameExist(String u_nickname, int u_seq);
    public int countUserIdByEmail(String u_email);
    public AdminUserDTO findUserByEmail(String u_email);
    public int countUserPwById(String u_id, String u_email);
    public void resetUserPw(String u_id, String u_pw, String u_email);
    public List<AdminUserDTO> getAllUsers(); // 반환 타입 변경
    public int deleteUserCompletely(int u_seq);
    public int reactivateUser(int u_seq);
}
