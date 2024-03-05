
	// 수량 증가 및 감소 버튼 이벤트 처리
	$("#plusBtn").click(function () {
		var currentValue = parseInt($("#pCount").val());
		$("#pCount").val(currentValue + 1);
		priceUpdate();
	});
	
	$("#minusBtn").click(function () {
		var currentValue = parseInt($("#pCount").val());
		if (currentValue > 1) {
			$("#pCount").val(currentValue - 1);
		}
		priceUpdate();
	});
	
	function priceUpdate(){
		let price = $("#pCount").val() * $("#priceUnit").val()
		$(".price").text(addComma(price));
	}
	
	$("#btnInsertCart").on("click", function(){
		let params = {
			p_code : $("#pCode").val(), // 상품코드 0001
			p_count : $("#pCount").val()
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
	
	$("#btnInsertOrder").on("click", function(){
		let params = {
			p_code : $("#pCode").val(), // 상품코드 0001
			p_count : $("#pCount").val()
		}
		
		$.ajax({
			url : "/order/insert",
			data : params,
			type : "post",
			dataType : "text",
			success : function(resultData){
				$("#cartModal2").modal('show');
			}
		})  
	})
	
	function toggleImageVisibility() {
	    var container = document.getElementById("image-container");
	    var button = document.getElementById("show-more");
	
	    if (container.classList.contains("expanded")) {
			container.classList.remove("expanded");
			container.style.maxHeight = "900px";
			button.innerText = "더보기";
	    } else {
			container.classList.add("expanded");
			container.style.maxHeight = "none"; // 이미지 전체 높이를 보여주기 위해 제한을 해제합니다.
			button.innerText = "접기";
	    }
    }
