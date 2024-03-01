/**
 * 
 */

$( function(){
	console.log("choice.js load");
	
	$(".btnResult").hide();
	$(".steps").eq(0).show();
	
	
	let keywordResult = {
		targetTable : null,
		nation : [],
		genre : [],
		year : [],
		region : [],
		theme : [],
		main : [],
		soup : [],
		spicy : []
	};
	
	
	$(".btnStepWork").on("click", function(){
		let currStep = $(this).attr("data-currStep");
		
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
				
				if(movie == true && show == true) {
					alert("한가지만 선택해주세요");
					return false;
				}
				
				if(movie == true){
					keywordResult.targetTable = "CG_MOVIE";
					$("#formWorkType").hide();
					$("#formMovieNation").show();
					$(this).attr("data-currStep", "choiceMovieNation");
				}
				
				if(show == true){
					keywordResult.targetTable = "CG_SHOW";
					$("#formWorkType").hide();
					$("#formShowGenre").show();
					$(this).attr("data-currStep", "choiceShowGenre");
				}
				
			break;
			
			// step 2.
			// 영화 : 국내영화 / 해외영화 선택
			case "choiceMovieNation" : 
				if($("#formMovieNation .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formMovieNation").hide();
					$("#formMovieGenre").show();
					if($("#selectAll_M1").is(":checked") == true){
						keywordResult.nation = ["all"];
					}else{
						for(var i = 0; i < $("#formMovieNation .items").length; i++){
							if($("#formMovieNation .items").eq(i).is(":checked") == true){
								keywordResult.nation.push($("#formMovieNation .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceMovieGenre");	
				}
			break;
			// 공연 : 장르 선택
			case "choiceShowGenre" :
				if($("#formShowGenre .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formShowGenre").hide();
					$("#formShowRegion").show();
					if($("#selectAll_S1").is(":checked") == true){
						keywordResult.genre = ["all"];
					}else{
						for(var i = 0; i < $("#formShowGenre .items").length; i++){
							if($("#formShowGenre .items").eq(i).is(":checked") == true){
								keywordResult.genre.push($("#formShowGenre .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceShowRegion");	
				}
			break;
			
			// step 3.
			// 영화 : 장르 선택
			case "choiceMovieGenre" :
				if($("#formMovieGenre .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formMovieGenre").hide();
					$("#formMovieYear").show();
					if($("#selectAll_M2").is(":checked") == true){
						keywordResult.genre = ["all"];
					}else{
						for(var i = 0; i < $("#formMovieGenre .items").length; i++){
							if($("#formMovieGenre .items").eq(i).is(":checked") == true){
								keywordResult.genre.push($("#formMovieGenre .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceMovieYear");
				}
			break;
			// 공연 : 지역 선택
			case "choiceShowRegion" :
				if($("#formShowRegion .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formShowRegion").hide();
					$("#formShowResult").show();
					if($("#selectAll_S2").is(":checked") == true){
						keywordResult.region = ["all"];
					}else{
						for(var i = 0; i < $("#formShowRegion .items").length; i++){
							if($("#formShowRegion .items").eq(i).is(":checked") == true){
								keywordResult.region.push($("#formShowRegion .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).hide();
					$(".btnResult").show();
					console.log(keywordResult);
				}
			break;
			
			// step 4.
			// 영화 : 년대 선택
			case "choiceMovieYear" :
				if($("#formMovieYear .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formMovieYear").hide();
					$(this).hide();
					if($("#selectAll_M3").is(":checked") == true){
						keywordResult.year = ["all"];
					}else{
						for(var i = 0; i < $("#formMovieYear .items").length; i++){
							if($("#formMovieYear .items").eq(i).is(":checked") == true){
								keywordResult.year.push($("#formMovieYear .items").eq(i).attr("value"));
							}	
						}
					}
					$(".btnResult").show();
					console.log(keywordResult);
				}
			break;
			// 공연 : 없음
		}
	});
		
	
	
	$("#selectAll_M1").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formMovieNation .items").prop("checked", true);
		}else{
			$("#formMovieNation .items").prop("checked", false);	
		}
	})
	
	$("#formMovieNation .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_M1").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formMovieNation .items").length; i++){
			if($("#formMovieNation .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_M1").prop("checked", true);
		}else{
			$("#selectAll_M1").prop("checked", false);
		}
	})
	
	$("#selectAll_M2").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formMovieGenre .items").prop("checked", true);
		}else{
			$("#formMovieGenre .items").prop("checked", false);	
		}
	})
	
	$("#formMovieGenre .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_M2").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formMovieGenre .items").length; i++){
			if($("#formMovieGenre .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_M2").prop("checked", true);
		}else{
			$("#selectAll_M2").prop("checked", false);
		}
	})
	
	$("#selectAll_M3").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formMovieYear .items").prop("checked", true);
		}else{
			$("#formMovieYear .items").prop("checked", false);	
		}
	})
	
	$("#formMovieYear .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_M3").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formMovieYear .items").length; i++){
			if($("#formMovieYear .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_M3").prop("checked", true);
		}else{
			$("#selectAll_M3").prop("checked", false);
		}
	})
	
	
	
	$("#selectAll_S1").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formShowGenre .items").prop("checked", true);
		}else{
			$("#formShowGenre .items").prop("checked", false);	
		}
	})
	
	$("#formShowGenre .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_S1").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formShowGenre .items").length; i++){
			if($("#formShowGenre .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_S1").prop("checked", true);
		}else{
			$("#selectAll_S1").prop("checked", false);
		}
	})
	
	$("#selectAll_S2").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formShowRegion .items").prop("checked", true);
		}else{
			$("#formShowRegion .items").prop("checked", false);	
		}
	})
	
	$("#formShowRegion .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_S2").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formShowRegion .items").length; i++){
			if($("#formShowRegion .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_S2").prop("checked", true);
		}else{
			$("#selectAll_S2").prop("checked", false);
		}
	})
	
	
	
	
	
	$(".btnStepFood").on("click", function(){
		let currStep = $(this).attr("data-currStep");
		keywordResult.targetTable = "CG_FOOD";
		
		switch(currStep){
			// food start
			case "choiceFoodTypeTheme" :
				if($("#formFoodTypeTheme .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formFoodTypeTheme").hide();
					$("#formFoodTypeMain").show();
					if($("#selectAll_F1").is(":checked") == true){
						keywordResult.theme = ["all"];
					}else{
						for(var i = 0; i < $("#formFoodTypeTheme .items").length; i++){
							if($("#formFoodTypeTheme .items").eq(i).is(":checked") == true){
								keywordResult.theme.push($("#formFoodTypeTheme .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceFoodTypeMain");	
				}
			break;
			case "choiceFoodTypeMain" :
				if($("#formFoodTypeMain .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formFoodTypeMain").hide();
					$("#formFoodTypeSoup").show();
					if($("#selectAll_F2").is(":checked") == true){
						keywordResult.main = ["all"];
					}else{
						for(var i = 0; i < $("#formFoodTypeMain .items").length; i++){
							if($("#formFoodTypeMain .items").eq(i).is(":checked") == true){
								keywordResult.main.push($("#formFoodTypeMain .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceFoodTypeSoup");	
				}	
			break;
			case "choiceFoodTypeSoup" :
				if($("#formFoodTypeSoup .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formFoodTypeSoup").hide();
					$("#formFoodTypeSpicy").show();
					if($("#selectAll_F3").is(":checked") == true){
						keywordResult.soup = ["all"];
					}else{
						for(var i = 0; i < $("#formFoodTypeSoup .items").length; i++){
							if($("#formFoodTypeSoup .items").eq(i).is(":checked") == true){
								keywordResult.soup.push($("#formFoodTypeSoup .items").eq(i).attr("value"));
							}	
						}
					}
					$(this).attr("data-currStep", "choiceFoodTypeSpicy");	
				}		
			break;
			case "choiceFoodTypeSpicy" :
				if($("#formFoodTypeSpicy .items").is(":checked") == false){
					alert("키워드를 선택 해주세요");
					return false;
				}else{
					$("#formFoodTypeSpicy").hide();
					$(this).hide();
					if($("#selectAll_F4").is(":checked") == true){
						keywordResult.spicy = ["all"];
					}else{
						for(var i = 0; i < $("#formFoodTypeSpicy .items").length; i++){
							if($("#formFoodTypeSpicy .items").eq(i).is(":checked") == true){
								keywordResult.spicy.push($("#formFoodTypeSpicy .items").eq(i).attr("value"));
							}	
						}
					}
					$(".btnResult").show();
					console.log(keywordResult);
				}		
			break;
		}
	})
	
	
	$("#selectAll_F1").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formFoodTypeTheme .items").prop("checked", true);
		}else{
			$("#formFoodTypeTheme .items").prop("checked", false);	
		}
	})
	
	$("#formFoodTypeTheme .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_F1").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formFoodTypeTheme .items").length; i++){
			if($("#formFoodTypeTheme .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_F1").prop("checked", true);
		}else{
			$("#selectAll_F1").prop("checked", false);
		}
	})
	
	$("#selectAll_F2").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formFoodTypeMain .items").prop("checked", true);
		}else{
			$("#formFoodTypeMain .items").prop("checked", false);	
		}
	})
	
	$("#formFoodTypeMain .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_F2").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formFoodTypeMain .items").length; i++){
			if($("#formFoodTypeMain .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_F2").prop("checked", true);
		}else{
			$("#selectAll_F2").prop("checked", false);
		}
	})
	
	$("#selectAll_F3").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formFoodTypeSoup .items").prop("checked", true);
		}else{
			$("#formFoodTypeSoup .items").prop("checked", false);	
		}
	})
	
	$("#formFoodTypeSoup .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_F3").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formFoodTypeSoup .items").length; i++){
			if($("#formFoodTypeSoup .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_F3").prop("checked", true);
		}else{
			$("#selectAll_F3").prop("checked", false);
		}
	})
	
	$("#selectAll_F4").on("click", function(){
		if($(this).is(":checked") == true){
			$("#formFoodTypeSpicy .items").prop("checked", true);
		}else{
			$("#formFoodTypeSpicy .items").prop("checked", false);	
		}
	})
	
	$("#formFoodTypeSpicy .items").on("click", function(){
		if($(this).is(":checked") == false){
			$("#selectAll_F4").prop("checked", false);
		}
		
		let chk = true;
		
		for(var i = 0; i < $("#formFoodTypeSpicy .items").length; i++){
			if($("#formFoodTypeSpicy .items").eq(i).is(":checked") == false){
				chk = false;	
			}
		}
		
		if(chk == true){
			$("#selectAll_F4").prop("checked", true);
		}else{
			$("#selectAll_F4").prop("checked", false);
		}
	})
	
	
	$(".btnResult").on("click", function(){
		
		$.ajax({
			url : "/keyword/makeResult",
			data : keywordResult,
			traditional: true,
			type : "post",
			success : function(returnData){
				console.log(returnData);
				location.href=returnData;
			},
			error : function(error){
				console.log(error);
			}
		})
		
	})
	
	
})