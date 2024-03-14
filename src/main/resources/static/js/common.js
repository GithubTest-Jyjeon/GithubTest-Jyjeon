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
	
})



function goBack() {
  window.history.back();
}
