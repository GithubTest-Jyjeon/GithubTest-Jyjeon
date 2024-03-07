package com.ex.springboot.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ex.springboot.dto.FoodDTO;
import com.ex.springboot.dto.MovieDTO;
import com.ex.springboot.dto.ResultDTO;
import com.ex.springboot.dto.ShowDTO;
import com.ex.springboot.interfaces.IresultDAO;

@Primary
@Repository
public class ResultDAO implements IresultDAO {

	@Autowired
	JdbcTemplate template;
	
	String whereStart = "(";
	String whereEnd = ")";
	
	@Override
	public int makeMovieResult(int b_seq, String targetTable, String genre, String nation, String year) {
		String whereNation = "";
		String whereGenre = "";
		String whereYear = "";
		
		ArrayList<String> keywords = new ArrayList<>();
		
		String selectQuery = "select "+targetTable+".* from "+targetTable;
		
		// 국가가 전체선택이 아닐 때
		if(!nation.equals("all")) {
			String[] arrNation = nation.split(",");
			int cntNation = arrNation.length;
			
			whereNation += whereStart;
			for(int i = 0; i < cntNation; i++) {
				
				if(arrNation[i].equals("K")) {
					keywords.add("M101");
				}else {
					keywords.add("M102");
				}
				
				whereNation += "cg_movie.m_nation_type = '"+arrNation[i]+"'";
				if(i != (cntNation - 1)) {
					whereNation += " or ";
				}
			}
			whereNation += whereEnd;
		}else {
			keywords.add("M100");
		}
		
		// 장르가 전체선택이 아닐 때
		if(!genre.equals("all")) {
			String[] arrGenre = genre.split(",");
			int cntGenre = arrGenre.length;
			
			String leftJoin = " left join ";
			
			// cg_movie_genre
			leftJoin += "cg_movie_genre on cg_movie_genre.m_code = cg_movie.m_code and (";
			for(int i = 0; i < cntGenre; i++) {
				String kGenreCode = template.queryForObject("select k_code from cg_keyword where k_word = '"+arrGenre[i]+"' and category_type = 'M'", String.class);
				keywords.add(kGenreCode);
				
				leftJoin += " cg_movie_genre.g_name = '"+arrGenre[i]+"' ";
				if(i != (cntGenre - 1)) {
					leftJoin += " or ";
				}
			}
			leftJoin += ")";
			
			whereGenre = "(cg_movie_genre.m_code = cg_movie.m_code)";
			
			selectQuery += leftJoin;
		}else {
			keywords.add("M200");
		}
		
		if(!genre.equals("all") || !nation.equals("all") || !year.equals("all")) {
			selectQuery += " where ";
		}
		
		// 년대가 전체선택이 아닐 때
		if(!year.equals("all")) {
			String[] arrYear = year.split(",");
			int cntYear = arrYear.length;
			
			whereYear += whereStart;
			for(int i = 0; i < cntYear; i++) {
				switch(arrYear[i]) {
					case "1999" : 
						keywords.add("M301");
						whereYear += "(cg_movie.m_year <= 1999)";
						break; 
					case "2000" :
						keywords.add("M302");
						whereYear += "(cg_movie.m_year >= 2000 and cg_movie.m_year < 2010)";
						break;
					case "2010" : 
						keywords.add("M303");
						whereYear += "(cg_movie.m_year >= 2010 and cg_movie.m_year < 2020)";
						break;
					case "2020" : 
						keywords.add("M304");
						whereYear += "(cg_movie.m_year >= 2020)";
						break;
				}
				
				if(i != (cntYear - 1)) {
					whereYear += " or ";
				}
			}
			whereYear += whereEnd;
		}else {
			keywords.add("M300");
		}
			
		selectQuery += whereNation + whereYear + whereGenre;
	
		selectQuery = selectQuery.replace(")(", ") and (");
		
		selectQuery += " order by cg_movie.m_year desc";
		
		int count = 0;
		String b_keywords = "";
	    while(keywords.size() > count) {
			b_keywords += keywords.get(count)+"|";
			count++;
	    }
	    
	    String b_results = "";
	    b_keywords = b_keywords.substring(0, b_keywords.length() - 1);
	    String updateQuery = "update cg_board set b_keywords = '"+b_keywords+"' where b_seq = "+b_seq;
	    template.update(updateQuery);
		
		ArrayList<MovieDTO> showArrayList = (ArrayList<MovieDTO>) template.query(selectQuery, new BeanPropertyRowMapper<MovieDTO>(MovieDTO.class));
		
		String insertQuery = "";
		int cnt = 0;
		
		while(showArrayList.size() > cnt) {
			b_results += showArrayList.get(cnt).getM_code()+"|";
			
			if((cnt+1) % 100 == 0 || (showArrayList.size()-1) == cnt) {
				b_results = b_results.substring(0, b_results.length() - 1);
				insertQuery = "insert into cg_result (r_seq, b_seq, t_code_arr) values (RESULT_SEQ.nextval, "+b_seq+", '"+b_results+"')";
				template.update(insertQuery);
				
				b_results = "";
			}
			
			cnt++;
		}
		
		return 0;
	}

	@Override
	public int makeShowResult(int b_seq, String targetTable, String genre, String region) {
		String whereGenre = "";
		String whereRegion = "";
		
		ArrayList<String> keywords = new ArrayList<>();
		
		String selectQuery = "select "+targetTable+".* from "+targetTable;
		
		if(!genre.equals("all")) {
			String[] arrGenre = genre.split(",");
			int cntGenre = arrGenre.length;
			
			String leftJoin = " left join ";
			
			// cg_show_genre
			leftJoin += "cg_show_genre on cg_show_genre.s_code = cg_show.s_code and (";
			for(int i = 0; i < cntGenre; i++) {
				String kGenreCode = template.queryForObject("select k_code from cg_keyword where k_word = '"+arrGenre[i]+"' and category_type = 'S'", String.class);
				keywords.add(kGenreCode);
				
				leftJoin += " cg_show_genre.g_name = '"+arrGenre[i]+"' ";
				if(i != (cntGenre - 1)) {
					leftJoin += " or ";
				}
			}
			leftJoin += ")";
			
			whereGenre = "(cg_show_genre.s_code = cg_show.s_code)";
			
			selectQuery += leftJoin;
		}else {
			keywords.add("S100");
		}
		
		if(!genre.equals("all") || !region.equals("all")) {
			selectQuery += " where ";
		}
		
		// 지역이 전체선택이 아닐 때
		if(!region.equals("all")) {
			String[] arrRegion = region.split(",");
			int cntRegion = arrRegion.length;
			
			whereRegion += whereStart;
			for(int i = 0; i < cntRegion; i++) {
				String selQuery = "select k_code from cg_keyword where k_word = '"+arrRegion[i]+"'";
				keywords.add(template.queryForObject(selQuery, String.class));
				whereRegion += "s_region = '"+arrRegion[i]+"'";
				if(i != (cntRegion - 1)) {
					whereRegion += " or ";
				}
			}
			whereRegion += whereEnd;
		}else {
			keywords.add("S200");
		}
		
		selectQuery += whereRegion + whereGenre;
	
		selectQuery = selectQuery.replace(")(", ") and (");
		
		int count = 0;
		String b_keywords = "";
	    while(keywords.size() > count) {
			b_keywords += keywords.get(count)+"|";
			count++;
	    }
	    
	    String b_results = "";
	    b_keywords = b_keywords.substring(0, b_keywords.length() - 1);
	    String updateQuery = "update cg_board set b_keywords = '"+b_keywords+"' where b_seq = "+b_seq;
	    template.update(updateQuery);
		
		ArrayList<ShowDTO> showArrayList = (ArrayList<ShowDTO>) template.query(selectQuery, new BeanPropertyRowMapper<ShowDTO>(ShowDTO.class));
		
		String insertQuery = "";
		int cnt = 0;
		
		while(showArrayList.size() > cnt) {
			b_results += showArrayList.get(cnt).getS_code()+"|";
			
			if((cnt+1) % 100 == 0 || (showArrayList.size()-1) == cnt) {
				b_results = b_results.substring(0, b_results.length() - 1);
				insertQuery = "insert into cg_result (r_seq, b_seq, t_code_arr) values (RESULT_SEQ.nextval, "+b_seq+", '"+b_results+"')";
				template.update(insertQuery);
				
				b_results = "";
			}
			cnt++;
		}
		template.update("update cg_board set b_total_count = "+cnt+" where b_seq = "+b_seq);
		
		return 0;
	}

	@Override
	public int makeFoodResult(int b_seq, String targetTable, String theme, String main, String soup, String spicy) {
		String whereTheme = "";
		String whereMain = "";
		String whereSoup = "";
		String whereSpicy = "";
		
		ArrayList<String> keywords = new ArrayList<>();
		
		String selectQuery = "select cg_food.f_code from "+targetTable;
		
		if(!theme.equals("all") || !main.equals("all") || !soup.equals("all") || !spicy.equals("all")) {
			selectQuery += " where ";
		}
		
		// 테마가 전체선택이 아닐 때
		if(!theme.equals("all")) {
			String[] arrTheme = theme.split(",");
			int cntTheme = arrTheme.length;
			
			whereTheme += whereStart;
			for(int i = 0; i < cntTheme; i++) {
				switch(arrTheme[i]){
					case "K" : keywords.add("F101"); break;
					case "U" : keywords.add("F102"); break;
					case "C" : keywords.add("F103"); break;
					case "J" : keywords.add("F104"); break;
					case "D" : keywords.add("F105"); break;
					case "E" : keywords.add("F106"); break;
				}
				
				whereTheme += "f_type_theme = '"+arrTheme[i]+"'";
				if(i != (cntTheme - 1)) {
					whereTheme += " or ";
				}
			}
			whereTheme += whereEnd;
		}else {
			keywords.add("F100");
		}
		
		// 주재료가 전체선택이 아닐 때
		if(!main.equals("all")) {
			String[] arrMain = main.split(",");
			int cntMain = arrMain.length;
			
			whereMain += whereStart;
			for(int i = 0; i < cntMain; i++) {
				switch(arrMain[i]){
					case "R" : keywords.add("F201"); break;
					case "B" : keywords.add("F202"); break;
					case "N" : keywords.add("F203"); break;
					case "M" : keywords.add("F204"); break;
					case "S" : keywords.add("F205"); break;
					case "V" : keywords.add("F206"); break;
				}
				
				whereMain += "f_type_main = '"+arrMain[i]+"'";
				if(i != (cntMain - 1)) {
					whereMain += " or ";
				}
			}
			whereMain += whereEnd;
		}else {
			keywords.add("F200");
		}
		
		// 국물종류가 전체선택이 아닐 때
		if(!soup.equals("all")) {
			String[] arrSoup = soup.split(",");
			int cntSoup = arrSoup.length;
			
			whereSoup += whereStart;
			for(int i = 0; i < cntSoup; i++) {
				if(arrSoup[i].equals("Y")) {
					keywords.add("F301");
				}else {
					keywords.add("F302");
				}
				
				whereSoup += "f_type_soup = '"+arrSoup[i]+"'";
				if(i != (cntSoup - 1)) {
					whereSoup += " or ";
				}
			}
			whereSoup += whereEnd;
		}else {
			keywords.add("F300");
		}
		
		// 국물종류가 전체선택이 아닐 때
		if(!spicy.equals("all")) {
			String[] arrSpicy = spicy.split(",");
			int cntSpicy = arrSpicy.length;
			
			whereSpicy += whereStart;
			for(int i = 0; i < cntSpicy; i++) {
				if(arrSpicy[i].equals("Y")) {
					keywords.add("F401");
				}else {
					keywords.add("F402");
				}
				
				whereSpicy += "f_type_spicy = '"+arrSpicy[i]+"'";
				if(i != (cntSpicy - 1)) {
					whereSpicy += " or ";
				}
			}
			whereSpicy += whereEnd;
		}else {
			keywords.add("F400");
		}
		
		selectQuery += whereTheme + whereMain + whereSoup + whereSpicy;
		
		selectQuery = selectQuery.replace(")(", ") and (");
		
		selectQuery += " order by cg_food.f_name desc";
		
		int count = 0;
		String b_keywords = "";
	    while(keywords.size() > count) {
			b_keywords += keywords.get(count)+"|";
			count++;
	    }
	    
	    String b_results = "";
	    b_keywords = b_keywords.substring(0, b_keywords.length() - 1);
	    String updateQuery = "update cg_board set b_keywords = '"+b_keywords+"' where b_seq = "+b_seq;
	    template.update(updateQuery);
		
		ArrayList<FoodDTO> foodArrayList = (ArrayList<FoodDTO>) template.query(selectQuery, new BeanPropertyRowMapper<FoodDTO>(FoodDTO.class));
		
		String insertQuery = "";
		int cnt = 0;
		
		while(foodArrayList.size() > cnt) {
			b_results += foodArrayList.get(cnt).getF_code()+"|";
			
			if((cnt+1) % 100 == 0 || (foodArrayList.size()-1) == cnt) {
				b_results = b_results.substring(0, b_results.length() - 1);
				insertQuery = "insert into cg_result (r_seq, b_seq, t_code_arr) values (RESULT_SEQ.nextval, "+b_seq+", '"+b_results+"')";
				template.update(insertQuery);
				
				b_results = "";
			}
			
			cnt++;
		}
		
		template.update("update cg_board set b_total_count = "+cnt+" where b_seq = "+b_seq);
		
		return 0;
	}
	
	
	public ArrayList<ResultDTO> resultList(int b_seq){
		String selectQuery = "select * from cg_result where b_seq = "+b_seq;
		
		return null;
	}

}
