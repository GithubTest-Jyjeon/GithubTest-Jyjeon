package com.ex.springboot.interfaces;

public interface IemailDAO {
    void sendNewPw(String to, String subject, String text);
}
