package com.ex.springboot.interfaces;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IheartDAO {
    
	public int getHeartCount(int f_seq);
}
