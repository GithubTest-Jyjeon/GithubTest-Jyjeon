package com.ex.springboot.interfaces;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.ReplyDTO;

@Mapper
public interface IreplyDAO {
    
	public int getReplyCount(int f_seq);
	public ArrayList<ReplyDTO> getReplyList(int f_seq);
	public void replyInsertProcess(int f_seq, int u_seq, String u_nickname, String fr_comment);
}
