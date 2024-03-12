/**
 * 
 */

$( function(){
	console.log("foodWrite.js load");
	
	let cnt = 1;
	
	
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
		
		let html  = '<span class="d-inline-block border border-light rounded-5 py-1 px-3 me-2">';
			html += '<span class="d-none ingredientCode" data-pcode="'+p_code+'"></span>';
			html += '<span class="d-none ingredientVolume" data-fvolume="'+f_volume+'"></span>';
			html += p_name+" "+f_volume;
			html += '</span>';
		
		$("#ingredientList").append(html).trigger("create");
		$("#f_volume").val("");
	})
	
	
	$("#btnRecipeAdd").on("click", function(){
		let f_recipe = $("#f_recipe").val();
		
		if(f_recipe == null || f_recipe == ""){
			alert("내용을 입력해주세요");
			return false;
		}
		
		let html  = '';
			html += '<div class="row">';
			html += '<div class="col-12 p-3">';
			html += '<div class="rounded-5 bg-primary text-white px-3 mb-2 d-inline-block">'+cnt+'</div>';
			html += '<div class="recipeItem">'+f_recipe+'</div>';
			html += '</div>';
			html += '</div>';
		
		$("#recipeList").append(html).trigger("create");
		
		$("#f_recipe").val("");
		cnt++;
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
		
		if($("#f_image").val() == null || $("#f_image").val() == ""){
			alert("이미지를 등록해주세요");
			return false;
		}
		
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
		
		let formData = new FormData($("#frmFoodWrite")[0]);
		let f_image = $("#f_image");
        let files = f_image[0].files; 
		
		let f_name = $("#f_name").val();
		
		$.ajax({
			url : "/food/writeProcess",
			data : formData,
			type : "post",
			cache : false,
			contentType : false,
			processData : false,
			success : function(returnData){
				alert("레시피 등록이 완료 되었습니다!");
				var result = confirm("레시피 보기 화면으로 이동하시겠습니까?");
				
				if(result){
					location.href="/food/view?f_code="+returnData;	
				}else{
					location.href="/";
				}
			},
			error : function(error){
				console.log(error);
				return false;	
			}
		})
	})
})