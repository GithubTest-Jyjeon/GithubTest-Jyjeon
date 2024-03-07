$(document).ready(function () {
	
	
	
      var swiper = new Swiper(".MainSwiper", {
        loop: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
      });
    
      var swiper = new Swiper(".mySwiper", {
        slidesPerView: 5,
        spaceBetween: 30,
        loop: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
        // 너비에 따른 설정을 추가하는 breakpoints 옵션
        breakpoints: {
          // 너비가 1100px 이하일 때
          1200: {
            slidesPerView: 8,
            spaceBetween: 20,
          },
          768: {
            slidesPerView: 6,
            spaceBetween: 10,
          },
          // 너비가 780px 이하일 때
          100: {
            slidesPerView: 3,
            spaceBetween: 5,
          },
        },
      });
});
