/**
 * 
 */

$( function(){
	console.log("admin - productView.js load");
	
	$("#btnDelete").on("click", function(){
		let result = confirm("상품을 삭제 하시겠습니까?");
		
		if(!result){
			return false;
		}
	})
	
})