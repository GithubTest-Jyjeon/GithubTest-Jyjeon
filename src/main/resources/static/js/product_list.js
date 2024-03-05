
	// 수량 증가 및 감소 버튼 이벤트 처리
	$("#plusBtn").click(function () {
		var currentValue = parseInt($("#input_p_count").val());
		$("#input_p_count").val(currentValue + 1);
	});
	
	$("#minusBtn").click(function () {
		var currentValue = parseInt($("#input_p_count").val());
		if (currentValue > 1) {
			$("#input_p_count").val(currentValue - 1);
		}
	});
	
	$(".btnCart").on("click", function(){
		var p_code = $(this).attr("data-bs-pcode");
		$("#input_p_count").val(1);
		
		$("#input_p_code").val(p_code);
		$("#cartModal").show();
		
		return false;
	})
	
	$("#btnInsertCart").on("click", function(){
		let params = {
			p_code : $("#input_p_code").val(), // 상품코드 0001
			p_count : $("#input_p_count").val()
		}
		
		$.ajax({
			url : "/cart/insertProduct",
			data : params,
			type : "post",
			dataType : "text",
			success : function(resultData){
				$("#cartModal2").modal('show');
			}
		})  
	})
