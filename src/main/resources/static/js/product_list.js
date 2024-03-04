
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
	if(resultData == 0){
	console.log("로그인 해야함");
	}else{
	console.log("장바구니에 담기 성공!");  
	}
	}
	})  
})


document.addEventListener('DOMContentLoaded', function () {
  const minusBtn = document.getElementById('minusBtn');
  const plusBtn = document.getElementById('plusBtn');
  const quantityInput = document.getElementById('input_p_count');

  minusBtn.addEventListener('click', function () {
    const currentValue = parseInt(quantityInput.value, 10);
    if (currentValue > 1) { // 최소 수량을 1로 설정
      quantityInput.value = currentValue - 1;
    }
  });

  plusBtn.addEventListener('click', function () {
    const currentValue = parseInt(quantityInput.value, 10);
    quantityInput.value = currentValue + 1;
  });
});
