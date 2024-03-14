/**
 * 
 */

$( function(){
	console.log("admin - productInsert.js load");
	
	p_price_calc();
	p_dc_yn_change();
	
	function p_price_calc(){
		let p_price = $("#p_price").val();
		let p_dc_percent = $("#p_dc_percent").val();
		
		let p_price_dc = Math.ceil(((p_price * 100) - (p_price * p_dc_percent)) / 100);
		
		$("#p_price_dc").val(p_price_dc);
	}
	
	function p_dc_yn_change(){
		let p_dc_yn = $("#p_dc_yn").val();
		
		if(p_dc_yn == 'N'){
			$("#p_dc_percent").attr("readonly", true);
			$("#p_dc_percent").addClass("text-decoration-line-through");
			$("#p_price_dc").val($("#p_price").val());
		}else{
			$("#p_dc_percent").attr("readonly", false);
			$("#p_dc_percent").removeClass("text-decoration-line-through");
			p_price_calc();
		}
	}
	
	function p_image_change(){
		let src = $("#p_image").val();
		$("#p_image_preview").attr("src", src);
	}
	
	$("#p_price").on("keyup", function(){
		p_price_calc();
	})
	
	$("#p_dc_yn").on("change", function(){
		p_dc_yn_change();
	})
	
	$("#p_dc_percent").on("keyup", function(){
		p_price_calc();
	})
	
	$("#p_dc_percent").on("change", function(){
		p_price_calc();
	})
	
	$("#p_image").on("change", function(){
		p_image_change();	
	})
	
	$("#btnReset").on("click", function(){
		setTimeout(function() {
    		p_dc_yn_change();
    		p_image_change();
  		}, 1);
	})
	
	$("#btnInsert").on("click", function(){
		let result = confirm("상품을 추가 하시겠습니까?");
		
		if(!result){
			return false;
		}
	})
	
})