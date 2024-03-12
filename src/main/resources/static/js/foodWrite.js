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
				html += '<option value="" selected disabled>재료를 선택해주세요</option>';
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
	})
	
	
	$("#btnRecipeAdd").on("click", function(){
		console.log("조리순서 추가 할거여");
		
		let f_recipe = $("#f_recipe").val();
		
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
		let formData = new FormData();
		let f_image = $("#f_image");
        let files = f_image[0].files; 
		
		let f_name = $("#f_name").val();
			
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
		
		formData.append("f_image", files);
		formData.append("f_name", f_name);
		formData.append("f_code_arr", f_code_arr);
		formData.append("f_volume_arr", f_volume_arr);
		formData.append("f_recipe", f_recipe);
		
		$.ajax({
			url : "/food/writeProcess",
			type : "post",
			data : formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success : function(returnData){
				console.log(returnData);
			},
			error : function(error){
				console.log(error);
			}
		})
		
		return false;
		
	})
})