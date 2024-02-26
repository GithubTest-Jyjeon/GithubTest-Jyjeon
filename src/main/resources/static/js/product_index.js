$(document).ready(function () {
  // 수량 증가 및 감소 버튼 이벤트 처리
  $("#plusBtn").click(function () {
    var currentValue = parseInt($("#quantityInput").val());
    $("#quantityInput").val(currentValue + 1);
  });

  $("#minusBtn").click(function () {
    var currentValue = parseInt($("#quantityInput").val());
    if (currentValue > 1) {
      $("#quantityInput").val(currentValue - 1);
    }
  });

  // 카드 클릭 이벤트
  $(".card").click(function (e) {
    // 이동을 원하는 조건을 여기에 명시
    if (!$("#staticBackdrop").hasClass("show")) {
      window.location.href = $(this).data("href");
    }
  });

  // 버튼 클릭 이벤트 중지
  $(".btn-primary, .btn-close, .btn-secondary").click(function (event) {
    event.stopPropagation();
  });

  // "더보기" 버튼 로직
  var initialCards = 4;
  var cards = $(".card");
  var totalCards = cards.length;

  // 초기 카드 표시
  cards.slice(0, initialCards).show();

  $("#loadMore").click(function (e) {
    e.preventDefault();
    var visibleCards = $(".card:visible").length;
    var next = visibleCards + 4; // 다음에 보여질 카드 수

    cards.slice(visibleCards, next).show();

    // 모든 카드가 표시된 경우 "더보기" 버튼 숨김
    if (next >= totalCards) {
      $(this).hide();
    }
  });

  // 모든 카드가 처음부터 보이면 "더보기" 버튼 숨김
  if (totalCards <= initialCards) {
    $("#loadMore").hide();
  }
  
  $(".btnCart").on("click", function(){
	  var p_code = $(this).attr("data-bs-pcode");
	  
	  $("#input_p_code").val(p_code);
	  $("#exampleModal").show();
	  
	  return false;
	  
  })
  
  $("#btnInsertCart").on("click", function(){
	  
	  let params = {
		  p_code : $("#input_p_code").val(), // 상품코드 0001
		  p_count : $("#input_p_count").val()
	  }
	  
	  console.log(params.p_code);
	  console.log(params.p_count);
	  
	  $.ajax({
		  url : "/cart/insertProduct",
		  data : params,
		  type : "post",
		  dataType : "text",
		  success : function(resultData){
			  console.log("장바구니에 담기 성공!");
		  }
	  })  
  })
});
