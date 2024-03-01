/**
 * 
 */

$( function(){
	console.log("cart.js load")
	
	// 총 가격 업데이트 함수
    function updateTotalPrice() {
		let cntItems = $(".chkboxItem").length;
		var priceResult = 0;
		
		$("#sumTotalPrice").text("0원");
		
		for(var idx = 0; idx < cntItems; idx++){
			if($(".chkboxItem").eq(idx).is(":checked")){
				var count = parseInt($(".quantityInput").eq(idx).val());
		        var dcPercent = parseInt($(".productDcPercent").eq(idx).val());
		        var price = parseInt($(".productPrice").eq(idx).val());
		        
		        priceResult += parseInt((price - (price * (dcPercent / 100))) * count);
			}
		}
		
		let result = addComma(priceResult);
		
		$("#sumTotalPrice").text(result+"원");
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
			if(!$(this).is(":checked")){
				chkboxTF = false;
			}
		}
		$("#chkboxAll").prop("checked", chkboxTF);
		updateTotalPrice();
	})
	
  
    $(".btnProductDelete").on("click", function() {
        let p_code = $(this).attr("data-pCode"); // 'data-p_code' 속성에서 상품 코드를 가져옵니다.
        
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

    
    
})