/**
 * 
 */

$( function(){
	console.log("jQuery Done!!");
	
	$("#btnIdExistCheck").on("click", function(){
		alert("당신이 입력한 아이디는 "+$("#u_id").val()+" 입니다.");
	})
})