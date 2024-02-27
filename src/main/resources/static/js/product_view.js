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
  
 
 
 
  $("#btnInsertCart").on("click", function(){
	  
	  let params = {
		  p_code : $("#input_p_code").val(), // 상품코드 0001
		  p_count : $("#input_p_count").val()
	  }
	  
	  console.log(params);
	  
	  $.ajax({
		  url : "/cart/cartInsertProductProcess",
		  data : params,
		  type : "post",
		  success : function(resultData){
			  if(resultData == 0){
				  alert("로그인 해야함");
			  }else{
				  alert("장바구니에 담기 성공!");  
			  }
		  },
		  error : function(error){
			  console.log(error);
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
    let price = $("#priceUnit").val();
    console.log(price);
    $(".price").text(price * quantityInput.value);
    console.log($(".price").text());
  });

  plusBtn.addEventListener('click', function () {
    const currentValue = parseInt(quantityInput.value, 10);
    quantityInput.value = currentValue + 1;
    let price = $("#priceUnit").val();
    console.log(price);
    $(".price").text(price * quantityInput.value);
    console.log($(".price").text());
  });
});







