/**
 * 
 */

$( function(){
	console.log("youtube.js load");
	
	var API_KEY = 'AIzaSyCSlHTD8mqUiU76ca8EIZdFZP4MaJPZHgo'; // 여기에 실제 API 키를 입력하세요.
	var SEARCH_KEYWORD = '집에서 ' + $('#SEARCH_KEYWORD').val() + ' 만드는 방법';
	var MAX_RESULTS = 3;
	
	var YOUTUBE_API_URL = 'https://www.googleapis.com/youtube/v3/search?key='+API_KEY+'&type=video&part=snippet&q='+encodeURIComponent(SEARCH_KEYWORD)+'&maxResults='+MAX_RESULTS;

	$.get(YOUTUBE_API_URL, function(data) {
		var items = data.items;
		var output = '';
		
		$.each(items, function(index, item) {
			var videoId = item.id.videoId;
			var title = item.snippet.title;
			var description = item.snippet.description.length > 100 ? item.snippet.description.substring(0, 100) + '...' : item.snippet.description;
			var thumbnailUrl = item.snippet.thumbnails.medium.url; //default/medium/high
			var videoUrl = 'https://www.youtube.com/watch?v='+videoId;
				output += '<div class="col-12">';
				output += 	'<a href="'+videoUrl+'" target="_blank" class="text-dark text-decoration-none">';
				output += 		'<div class="card border-light mb-3" style="max-width: 100%;">';
				output += 			'<div class="row g-0">';
				output += 				'<div class="col-md-4">';
				output += 					'<img src="'+thumbnailUrl+'" class="img-fluid rounded-start" style="width: 100%; height: 100%;" alt="Thumbnail" target="_blank">';
				output += 				'</div>';
				output += 			'<div class="col-md-8">';
				output += 				'<div class="card-body">';
				output += 					'<h5 class="card-title">'+title+'</h5>';
				output += 					'<p class="card-text">'+description+'</p>';
				output += 					'</div>';
				output += 				'</div>';
				output += 			'</div>';
				output += 		'</div>';
				output += 	'</a>';
				output += '</div>';
			});
			
		$('.youtube').html(output).trigger("create");
	});
	
	
})