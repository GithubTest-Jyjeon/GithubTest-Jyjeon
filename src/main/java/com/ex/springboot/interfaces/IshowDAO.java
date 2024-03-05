package com.ex.springboot.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.springboot.dto.ShowDTO;

@Mapper
public interface IshowDAO {
	ShowDTO showDTO = new ShowDTO();
	
	public ArrayList<ShowDTO> showList(int page, int b_seq, int u_seq);
	public ShowDTO showView(String s_code);
	public int showTotal(int b_seq, int u_seq);

	List<String> getActorsByS_code(String s_code);
	List<String> getDirectorsByS_code(String s_code);
}