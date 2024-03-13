/**
 * 
 */

$( function(){
	console.log("foodWrite.js load");
	
	$("#f_image").on("change", function(event) {

	    var file = event.target.files[0];
	    var extension = file.name.split(".").pop().toLowerCase();
	    var pass = false;
	    
	    switch(extension){
			case "jpg" : pass = true; break;
			case "jpeg" : pass = true; break;
			case "png" : pass = true; break;
			case "gif" : pass = true; break;
		}
		
		if(pass == false){
			alert("이미지 파일만 업로드 해주세요");
			$("#f_image").val("");
			return false;
		}
	
	    var reader = new FileReader(); 
	    	reader.onload = function(e) {
				$("#f_image_preview").attr("src", e.target.result);
	    	}
	    	reader.readAsDataURL(file);
	});
	
	
	
	
	$("#p_category").on("change", function(){
		$("#productList").empty();
		
		let p_category = $("#p_category").val();
		
		let html  = '';
		
		$.ajax({
			url : "/product/listForCategory",
			data : {"p_category" : p_category},
			type : "get",
			success : function(returnData){
				let cnt = returnData.length;
				for(var i = 0; i < cnt; i++){
					html += '<option value="'+returnData[i].p_code+'">'+returnData[i].p_name+'</option>';	
				}
				$("#productList").append(html).trigger("create");
			},
			error : function(error){
				console.log(error);
			}
		})
		
	})
	
	let cntIngredient = $(".ingredientParent").length;
	
	$("#btnIngredientAdd").on("click", function(){
		let p_name = $("#productList option:selected").text();
		let p_code = $("#productList option:selected").val();
		let f_volume = $("#f_volume").val();
		
		if(p_name == null || p_name == ""){
			alert("재료를 선택해주세요");
			return false;
		}
		
		if(f_volume.replace(/^\s*/, "") == null || f_volume.replace(/^\s*/, "") == ""){
			alert("용량을 입력해주세요");
			return false;
		}
		
		let html  = '<span class="d-inline-block border border-light rounded-5 py-1 px-3 me-2 ingredientParent">';
			html += 	'<span class="d-none ingredientCode" data-pcode="'+p_code+'"></span>';
			html += 	'<span class="d-none ingredientVolume" data-fvolume="'+f_volume+'"></span>';
			html += 	p_name+' '+f_volume+' '+'<span class="ingredientDel text-primary" idx='+cntIngredient+' role="button">X</span>';
			html += '</span>';
		
		cntIngredient++;
		
		$("#ingredientList").append(html).trigger("create");
		$("#f_volume").val("");
	})
	
	let cntRecipe = $(".recipeParent").length;
	
	$("#btnRecipeAdd").on("click", function(){
		let f_recipe = $("#f_recipe").val();
		
		if(f_recipe == null || f_recipe == ""){
			alert("내용을 입력해주세요");
			return false;
		}
		
		let html  = '';
			html += '<div class="row recipeParent">';
			html += 	'<div class="col-12 p-3">';
			html += 		'<div class="rounded-5 bg-primary text-white px-3 mb-2 d-inline-block recipeNum">'+(cntRecipe+1)+'</div>';
			html +=			'<span class="recipeDel text-primary ms-3" idx='+cntRecipe+' role="button">X</span>';
			html += 		'<div class="recipeItem">';
			html += 			f_recipe;
			html += 		'</div>';
			html += 	'</div>';
			html += '</div>';
		
		$("#recipeList").append(html).trigger("create");
		
		$("#f_recipe").val("");
		cntRecipe++;
	})
	
	
	$("#btnRecipeAddDone").on("click", function(){
		let f_code_arr = '';
		let f_volume_arr = '';
		let f_recipe = '';
		
		let f_code_count = $(".ingredientCode").length;
		let f_volume_count = $(".ingredientVolume").length;
		let f_recipe_count = $(".recipeItem").length;
		
		for(var i = 0; i < f_code_count; i++){
			f_code_arr += $(".ingredientCode").eq(i).attr("data-pcode")+"|";
		}
		f_code_arr = f_code_arr.slice(0, -1);
		
		for(var j = 0; j < f_volume_count; j++){
			f_volume_arr += $(".ingredientVolume").eq(j).attr("data-fvolume")+"|";
		}
		f_volume_arr = f_volume_arr.slice(0, -1);
		
		for(var k = 0; k < f_recipe_count; k++){
			f_recipe += $(".recipeItem").eq(k).text()+"|";
		}
		
		f_recipe = f_recipe.slice(0, -1);
		
		if($("#f_name").val().replace(/^\s*/, "") == null || $("#f_name").val().replace(/^\s*/, "") == ""){
			alert("음식명을 입력해주세요");
			return false;
		}
		
		if($("input[name=f_type_theme]").is(":checked") == false){
			alert("음식테마 키워드를 선택 해주세요");
			return false;
		}
		if($("input[name=f_type_main]").is(":checked") == false){
			alert("주 재료 키워드를 선택 해주세요");
			return false;
		}
		if($("input[name=f_type_soup]").is(":checked") == false){
			alert("국물있음/국물없음 키워드를 선택 해주세요");
			return false;
		}
		if($("input[name=f_type_spicy]").is(":checked") == false){
			alert("매움/안매움 키워드를 선택 해주세요");
			return false;
		}
		
		if(f_code_arr == null || f_code_arr == ""){
			alert("재료를 하나 이상 등록해주세요");
			return false;
		}
		
		if(f_recipe == null || f_recipe == ""){
			alert("조리 순서를 하나 이상 입력해주세요");
			return false;
		}
		
		$("#f_recipe").val(f_recipe);
		$("#f_code_arr").val(f_code_arr);
		$("#f_volume_arr").val(f_volume_arr);
		
		let formData = new FormData($("#frmFoodUpdate")[0]);
		let f_image = $("#f_image");
        let files = f_image[0].files;
        
		let f_name = $("#f_name").val();
		
		$.ajax({
			url : "/food/updateProcess",
			data : formData,
			type : "post",
			cache : false,
			contentType : false,
			processData : false,
			success : function(returnData){
				switch(returnData){
					
					case "errorFileSize" : 
						alert("파일 용량은 5MB 이하로 업로드 하시기 바랍니다.");
						return false;
					
					case "errorUpdate" : 
						alert("레시피 수정에 실패 하였습니다.");
						return false;
					
					case "errorUserDTO" :
						alert("로그인 후 수정 하시기 바랍니다."); 
						return false;
					
					default :
						alert("레시피 수정이 완료 되었습니다!");
						var result = confirm("레시피 보기 화면으로 이동하시겠습니까?");
						
						if(result){
							location.href="/food/view?f_code="+returnData;	
						}else{
							location.href="/";
						}
				}
			},
			error : function(error){
				console.log(error);
				return false;	
			}
		})
	})
	
	$(document).on("click", ".recipeDel", function(){
		let idx = $(this).attr("idx");
		$(".recipeParent").eq(idx).remove();
		for(var i = idx; i < $(".recipeDel").length; i++){
			var num = Number(i) + Number(1);
			$(".recipeDel").eq(i).attr("idx", i);
			$(".recipeNum").eq(i).text(num);
		}
		cntRecipe--;
	})
	
	
	$(document).on("click", ".ingredientDel", function(){
		let idx = $(this).attr("idx");
		$(".ingredientParent").eq(idx).remove();
		for(var i = idx; i < $(".ingredientDel").length; i++){
			$(".ingredientDel").eq(i).attr("idx", i);
		}
		cntIngredient--;
	})
})