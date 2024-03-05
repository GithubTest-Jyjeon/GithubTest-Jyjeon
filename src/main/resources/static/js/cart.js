/**
 * 
 */

$( function(){
	console.log("cart.js load")
	
	// 총 가격 업데이트 함수
    function updateTotalPrice() {
		let cntItems = $(".chkboxItem").length;
		var priceResult = 0;
		
		$("#sumTotalPrice").text("0");
		
		for(var idx = 0; idx < cntItems; idx++){
			if($(".chkboxItem").eq(idx).is(":checked")){
				var count = parseInt($(".quantityInput").eq(idx).val());
		        var dcPercent = parseInt($(".productDcPercent").eq(idx).val());
		        var price = parseInt($(".productPrice").eq(idx).val());
		        
		        priceResult += parseInt((price - (price * (dcPercent / 100))) * count);
			}
		}
		
		$("#sumTotalPrice").attr("data-val", priceResult);
		
		let result = addComma(priceResult);
		$("#sumTotalPrice").text(result);
    }
    
    function updateSummary(idx) {
        var count = $(".quantityInput").eq(idx).val();
        var dcPercent = $(".productDcPercent").eq(idx).val();
        var price = $(".productPrice").eq(idx).val();
        var priceResult = price;
        if(dcPercent > 0){
			priceResult = price - (price * (dcPercent / 100));	
		}
        
        let result = addComma(priceResult * count);
        
        $(".total-price").eq(idx).text(result + "원");
	}
	
	// AJAX 호출을 통한 수량 업데이트
    function cartUpdateProductProcess(p_code, p_count) {
        $.ajax({
            url: '/cart/updateProduct',
            type: 'POST',
            data: {
                p_code: p_code,
                p_count: p_count
            } 
        });
    }
	

    // 맨 위의 체크박스에 대한 클릭 이벤트 리스너 추가
    $("#chkboxAll").on("change", function() {
		let chkboxTF = $(this).is(":checked"); 
		// 맨 위의 체크박스 상태에 따라 모든 체크박스 상태를 동일하게 설정
		for(let i = 0; i < $(".chkboxItem").length; i++) {
			$(".chkboxItem").eq(i).prop("checked", chkboxTF);
		}
		
		updateTotalPrice();
    })
    
    $(".chkboxItem").on("change", function(){
		let chkboxTF = true;
		
		for(let i = 0; i < $(".chkboxItem").length; i++) {
			if(!$(".chkboxItem").eq(i).is(":checked")){
				chkboxTF = false;
			}
		}
		$("#chkboxAll").prop("checked", chkboxTF);
		updateTotalPrice();
	})
	
  
    $(".btnProductDelete").on("click", function() {
        let p_code = $(this).attr("data-pCode"); // 'data-p_code' 속성에서 상품 코드를 가져옵니다.
        
        let confirm = confirm("상품을 장바구니에서 삭제 하시겠습니까?");
        
        if(confirm){
			$.ajax({
	            url: '/cart/deleteProduct',
	            type: 'POST',
	            data: { p_code: p_code },
	            success: function(result) {
	                if(result > 0) {
	                    alert("상품이 성공적으로 삭제되었습니다.");
	                    location.reload(); // 페이지 새로고침
	                } else {
	                    alert("상품 삭제에 실패했습니다.");
	                }
	            },
	            error: function(xhr, status, error) {
	                // 오류 발생 시 오류 메시지 출력
	                alert("오류 발생: " + xhr.responseText);
	            }
	        });	
		}
    });

    // 수량 증가 버튼 클릭 이벤트
    $(".plusBtn").click(function() {
        var idx = $(this).parent().attr("data-idx");
        var quantityInput = $(".quantityInput").eq(idx);
        var currentValue = parseInt(quantityInput.val());
        var newValue = currentValue + 1;
        var p_code = $(".productItem").eq(idx).attr("p_code");

        quantityInput.val(newValue);
        cartUpdateProductProcess(p_code, newValue);
        
        updateSummary(idx);
        updateTotalPrice();
    });
    
    // 수량 감소 버튼 클릭 이벤트
    $(".minusBtn").click(function() {
        var idx = $(this).parent().attr("data-idx");
        var quantityInput = $(".quantityInput").eq(idx);
        var currentValue = parseInt(quantityInput.val());
        var newValue = currentValue - 1;
        var p_code = $(".productItem").eq(idx).attr("p_code");
        
        if (newValue == 0) {
            newValue = 1;
        }
        
        quantityInput.val(newValue);
        cartUpdateProductProcess(p_code, newValue);
        
        updateSummary(idx);
        updateTotalPrice();
    });
    
    // 수량 직접 변경 시 이벤트
	$(".quantityInput").on("input", function(){
		var idx = $(this).parent().attr("data-idx");
		var newValue = $(this).val();
		var p_code = $(".productItem").eq(idx).attr("p_code");
		
		cartUpdateProductProcess(p_code, newValue);
        
        updateSummary(idx);
	})


	$("#btnOrder").on("click", function(){
		let chkboxItemCount = $(".chkboxItem").length;
		
		let params = {
			productInfo : [],
			totalPrice : 0
		}
		
		let chkZero = $(".chkboxItem").is(":checked");
		
		if(chkZero == false){
			alert("주문 할 상품을 체크 해주세요");
			return false;
		}
		
		let html  = '';
			html += '<div class="row fw-bold">';
			html += '<div class="col-4">상품명</div>';
			html += '<div class="col-3">단가</div>';
			html += '<div class="col-2">수량</div>';
			html += '<div class="col-3 text-end">총액</div>';
			html += '</div>';
			html += '<hr />';
		
		for(let i = 0; i < chkboxItemCount; i++){
			
			if($(".chkboxItem").eq(i).is(":checked") == true){
				let pCount = parseInt($(".quantityInput").eq(i).val());
				let pCode = $(".productCode").eq(i).val();
				let pName = $(".productName").eq(i).val();
				let pPriceOrigin = $(".productPrice").eq(i).val();
				let pDcPercent = $(".productDcPercent").eq(i).val();
				let pPriceUnit = pPriceOrigin - (pPriceOrigin * pDcPercent) / 100;
				
				params.productInfo.push({pCount, pCode});
				
				html += '<div class="row">';
				html += '<div class="col-4">'+pName+'</div>';
				html += '<div class="col-3 text-end">'+addComma(pPriceUnit)+'</div>';
				html += '<div class="col-2 text-center">'+pCount+'</div>';
				html += '<div class="col-3 text-end">'+addComma(pPriceUnit * pCount)+'</div>';
				html += '</div>';
				html += '<hr class="hr2" />';
			}
		}
		
		let totalPrice = parseInt($("#sumTotalPrice").attr("data-val"));
		
		html += '<div class="row">';
		html += '<div class="col-4 offset-8 fw-bold text-end">'+addComma(totalPrice)+'</div>';
		html += '</div>';
		
		params.totalPrice = addComma(totalPrice);
		
		$("#modalInvoice .modal-body").html(html);
		
		$("#modalInvoice").modal("show");
	})
	
	$("#btnOrderDone").on("click", function(){
		let chkboxItemCount = $(".chkboxItem").length;
		
		let params = {
			productInfo : [],
			totalPrice : 0
		}
		
		for(let i = 0; i < chkboxItemCount; i++){
			
			if($(".chkboxItem").eq(i).is(":checked") == true){
				let pCount = parseInt($(".quantityInput").eq(i).val());
				let pCode = $(".productCode").eq(i).val();
				let pName = $(".productName").eq(i).val();
				let pPrice = $(".productPrice").eq(i).val();
				let pDcPercent = $(".productDcPercent").eq(i).val();
				let finalPrice = pPrice - (pPrice * pDcPercent) / 100;
				
				params.productInfo.push({pCount, pCode, finalPrice});				
			}
		}
		
		let totalPrice = parseInt($("#sumTotalPrice").attr("data-val"));
		params.totalPrice = addComma(totalPrice);
		
		$.ajax({
			url : "/order/insert",
			/*type : "post",
			data : {orderData : params},*/
			success : function(returnData){
				console.log(returnData);
			},
			error : function(error){
				console.log(error);
			}		
		})
	})
    
    
})