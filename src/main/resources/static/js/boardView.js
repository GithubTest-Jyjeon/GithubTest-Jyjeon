/**
 * 
 */


$( function(){
	console.log("boardView.js load");
	
	let b_seq = $("#b_seq").val();
	
	$("#b_share_y").on("click", function(){
		$.ajax({
			url : "/board/updateShare",
			data : {"b_seq" : b_seq, "b_share_yn" : 'Y'},
			type : "post",
			success : function(){
				window.reload();
			}
		})
	})
	
	$("#b_share_n").on("click", function(){
		$.ajax({
			url : "/board/updateShare",
			data : {"b_seq" : b_seq, "b_share_yn" : 'N'},
			type : "post",
			success : function(){
				window.reload();
			}
		})
	})
	
})