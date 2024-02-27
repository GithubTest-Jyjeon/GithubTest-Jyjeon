/**
 * 
 */

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
})