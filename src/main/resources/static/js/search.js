/**
 * 
 */

function addComma(val){
	return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}

$( function(){
	console.log("common.js load");
	
	let globalNav = $("#globalNav");
	
	$(window).scroll( function(){
		let y = $(document).scrollTop();
		if(y <= 70){
			globalNav.removeClass("fixed-top");
		}else{
			globalNav.addClass("fixed-top");
		}
	})
	
	$("#btnTop").on("click", function(){
		$(window).scrollTop(0);
	})
	
	let cnt = $(".f_seq").length;
      for(let i = 0; i < cnt; i++){
		  
		  let f_seq = $(".f_seq").eq(i).val();
		  
		  $.ajax({
			url : "/food/heartCount",
			data : {"f_seq" : f_seq},
			type : "get",
			success : function(returnData){
				$(".f_heart").eq(i).text(returnData);
			}
		  })
		  
		  $.ajax({
			url : "/food/replyCount",
			data : {"f_seq" : f_seq},
			type : "get",
			success : function(returnData){
				$(".f_reply").eq(i).text(returnData);
			}
		  })
	  }
	
  })