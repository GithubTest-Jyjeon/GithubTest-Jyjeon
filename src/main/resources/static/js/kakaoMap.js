/**
 * 
 */

$( function(){
	console.log("kakaoMap.js load");
	
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var lat = position.coords.latitude, // 위도
				lon = position.coords.longitude; // 경도

			var infowindow = new kakao.maps.InfoWindow({
				zIndex : 1
			});
			
			var mapContainer = document.getElementById('map'), mapOption = {
				center : new kakao.maps.LatLng(lat, lon)
			};

			var map = new kakao.maps.Map(mapContainer, mapOption);
			var ps = new kakao.maps.services.Places();

			ps.keywordSearch(keyword + ' 맛집', placesSearchCB);

			var keyword = document.getElementById("searchText").value;
			ps.keywordSearch(keyword, placesSearchCB, {
				location : new kakao.maps.LatLng(lat, lon)
			});

			function placesSearchCB(data, status, pagination) {
				if (status === kakao.maps.services.Status.OK) {
					var bounds = new kakao.maps.LatLngBounds();

					for (var i = 0; i < data.length; i++) {
						displayMarker(data[i]);
						bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
					}

					map.setBounds(bounds);
				}
			}

			function displayMarker(place) {
				var marker = new kakao.maps.Marker({
					map : map,
					position : new kakao.maps.LatLng(place.y, place.x)
				});

				kakao.maps.event.addListener(marker, 'click', function() {
					infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
					infowindow.open(map, marker);
				});
			}
		})
	}
	
})