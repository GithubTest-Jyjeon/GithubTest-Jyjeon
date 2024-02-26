/**
 * 
 */

$( function(){
	console.log("choice.js load");
	
	$(".btnResult").hide();
	$(".steps").hide();
	$(".steps").eq(0).show();
	
	
	let keywordArray = []; 
	
	$(".btnStep").on("click", function(){
		let currStep = $(".btnStep").attr("data-currStep");
		
		switch(currStep) {
			// step 1.
			// 영화 / 공연 선택
			case "choiceWorkType" :
				let movie = $("#checkBoxMovie").is(":checked");
				let show = $("#checkBoxShow").is(":checked");
				
				if(movie == false && show == false) {
					alert("영화 또는 공연을 선택해주세요");
					return false;
				}
				
				if(movie == true){
					keywordArray.push("CG_MOVIE");
					$("#formWorkType").hide();
					$("#formMovieNation").show();
					$(".btnStep").attr("data-currStep", "choiceMovieNation");
				}
				
				if(show == true){
					keywordArray.push("CG_SHOW");
					$("#formWorkType").hide();
					$("#formShowGenre").show();
					$(".btnStep").attr("data-currStep", "choiceShowGenre");
				}
				
			break;
			
			// step 2.
			// 영화 : 국내영화 / 해외영화 선택
			case "choiceMovieNation" : 
				$("#formMovieNation").hide();
				$("#formMovieGenre").show();
				$(this).attr("data-currStep", "choiceMovieGenre");
			break;
			// 공연 : 장르 선택
			case "choiceShowGenre" :
				$("#formShowGenre").hide();
				$("#formShowRegion").show();
				$(this).attr("data-currStep", "choiceShowRegion");	
			break;
			
			// step 3.
			// 영화 : 장르 선택
			case "choiceMovieGenre" :
				$("#formMovieGenre").hide();
				$("#formMovieYear").show();
				$(this).attr("data-currStep", "choiceMovieYear");
			break;
			// 공연 : 지역 선택
			case "choiceShowRegion" :
				$("#formShowRegion").hide();
				$("#formShowResult").show();
				$(this).hide();
				$(".btnResult").show();
			break;
			
			// step 4.
			// 영화 : 년대 선택
			case "choiceMovieYear" :
				$("#formMovieYear").hide();
				$(this).hide();
				$(".btnResult").show();
			break;
			// 공연 : 없음

			
			case "choiceFoodTypeTheme" : break;
			case "choiceFoodTypeMain" : break;
			case "choiveFoodTypeSoup" : break;
			case "choiveFoodTypeSpicy" : break;
		}
		
	})
	
	$("#selectAll").on("click", function(){
		$(".checkbox-input").prop("checked", true);
	})
	
})