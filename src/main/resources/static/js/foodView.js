/**
 * 
 */

$( function(){
	console.log("foodView.js load");
	
	let f_seq = $("#f_seq").val();
	
	$.ajax({
		url : "/food/heartCheck",
		data : {f_seq : f_seq},
		type : "get",
		success : function(returnData){
			if(returnData != 0){
				$("#btnHeartOff").removeClass("d-none");
			}else{
				$("#btnHeartOn").removeClass("d-none");
			}
		}
	})
	
	$(".card-body").each(function() {
		var maxSpanCount = 3; // 최대 표시할 span 태그의 수
		var spans = $(this).find(".R_category");
		if (spans.length > maxSpanCount) {
			spans.slice(maxSpanCount).hide(); // 초과하는 span 숨기기
			$(this).find(".see-more").show().text("..."); // "상세카테고리" 링크 표시
		} else {
			$(this).find(".see-more").hide(); // span 개수가 maxSpanCount 이하면 "더보기" 링크 숨기기
		}
	});

	$(".see-more").click(function(e) {
		e.preventDefault();
		var isVisible = $(this).text() === "...";
		$(this).parent().find(".R_category").slice(3).toggle(); // 숨겨진 .R_category 토글
		$(this).text(isVisible ? "접기" : "..."); // 버튼 텍스트 토글
	});
	
	
	$("#btnHeartOn").on("click", function(){
		
		let params = {
			f_seq : f_seq,
		}
		
		$.ajax({
			url : "/food/heartOn",
			data : params,
			type : "post",
			success : function(returnData){
				if(returnData != 0){
					$("#btnHeartOn").addClass("d-none");
					$("#btnHeartOff").removeClass("d-none");
					$("#heartMsg").text("레시피를 찜 하였습니다");
					$("#heartModal").modal("show");
					getHeartAndReplyCount(f_seq);
				}else{
					alert("로그인 하시기 바랍니다.");
					location.href="/user/login";
				}
			}
		})
	})
	
	
	$("#btnHeartOff").on("click", function(){
		
		let params = {
			f_seq : f_seq,
		}
		
		$.ajax({
			url : "/food/heartOff",
			data : params,
			type : "post",
			success : function(returnData){
				if(returnData != 0){
					$("#btnHeartOn").removeClass("d-none");
					$("#btnHeartOff").addClass("d-none");
					$("#heartMsg").text("레시피를 찜 해제 하였습니다");
					$("#heartModal").modal("show");
					getHeartAndReplyCount(f_seq);
				}else{
					alert("로그인 하시기 바랍니다.");
					location.href="/user/login";
				}
			}
		})
	})
	
	getHeartAndReplyCount(f_seq);
	
	function getHeartAndReplyCount(f_seq){
		  
	  $.ajax({
		url : "/food/heartCount",
		data : {"f_seq" : f_seq},
		type : "get",
		success : function(returnData){
			$(".f_heart").text(returnData);
		}
	  })
	  
	  $.ajax({
		url : "/food/replyCount",
		data : {"f_seq" : f_seq},
		type : "get",
		success : function(returnData){
			$(".f_reply").text(returnData);
		}
	  })
	}
	
	$("#btnInsertReply").on("click", function(){
		
		let fr_comment = $("#fr_comment").val();
		if(!fr_comment){
			alert("댓글 코멘트를 입력해주세요");
			return false;
		}else{
			$.ajax({
				url : "/food/replyInsert",
				data : {"f_seq" : f_seq, "fr_comment" : fr_comment},
				type : "post",
				success : function(returnData){
					location.reload();
				},
				error : function(error){
					console.log(error);
				}
			})
		}
	})
})