/**
 * 
 */

$( function(){
	console.log("join.js load");
	
	let existCheckId = false;
	let existCheckNickname = false;
	
	let pwRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/;
	let nicknameRegex = /^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/;
	let emailRegex = /^(?:\w+\.?)*\w+@(?:\w+\.)+\w+$/;
	let phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
	
	$("input[name=u_id]").on("change", function(){
		existCheckId = false;			
	})
	
	$("input[name=u_nickname]").on("change", function(){
		existCheckNickname = false;			
	})
	
	$("#btnJoin").on("click", function(){
		let u_id = $("input[name=u_id]");
		let u_pw = $("input[name=u_pw]");
		let u_pw_chk = $("input[name=u_pw_chk]");
		let u_nickname = $("input[name=u_nickname]");
		let u_name = $("input[name=u_name]");
		let u_email = $("input[name=u_email]");
		let u_birth = $("input[name=u_birth]");
		let u_postcode = $("input[name=u_postcode]");
		let u_address = $("input[name=u_address]");
		let u_address_detail = $("input[name=u_address_detail]");
		let u_address_extra = $("input[name=u_address_extra]");
		let u_phone = $("input[name=u_phone]");
		let u_gender_m = $("input[name=u_gender_m]");
		let u_gender_w = $("input[name=u_gender_w]");
		
		let joinParams = {
			u_id : null,
			u_pw : null,
			u_nickname : null,
			u_name : null,
			u_email : null,
			u_birth : null,
			u_postcode : null,
			u_address : null,
			u_address_detail : null,
			u_address_extra : null,
			u_phone : null,
			u_gender : null
		}
		
		if(u_id.val().replace(/^\s*/, "") == null || u_id.val().replace(/^\s*/, "") == ""){
			alert("아이디를 입력하세요");
			u_id.addClass("border-danger");
			u_id.focus();
			return false;
		}else{
			u_id.removeClass("border-danger");
			u_id.addClass("border-success");
		}
		
		if(!existCheckId){
			alert("아이디 중복체크를 하세요");
			u_id.removeClass("border-success");
			u_id.addClass("border-danger");
			return false;
		}else{
			joinParams.u_id = u_id.val().replace(/^\s*/, "");
		}
		
		if(!pwRegex.test(u_pw.val().replace(/^\s*/, ""))){
			alert("비밀번호는 8~16자 영문 대/소문자, 숫자, 특수문자를 조합하여 입력하세요");
			u_pw.addClass("border-danger");
			u_pw.focus();
			return false;
		}else{
			u_pw.removeClass("border-danger");
			u_pw_chk.addClass("border-success");
		}
		
		if(u_pw.val() != u_pw_chk.val()){
			alert("비밀번호가 일치하지 않습니다");
			u_pw.addClass("border-danger");
			u_pw_chk.addClass("border-danger");
			u_pw_chk.focus();
			return false;
		}else{
			u_pw.removeClass("border-danger");
			u_pw.addClass("border-success");
			u_pw_chk.removeClass("border-danger");
			u_pw_chk.addClass("border-success");
			
			joinParams.u_pw = u_pw.val();
		}
		
		if(u_nickname.val().replace(/^\s*/, "") == null || u_nickname.val().replace(/^\s*/, "") == ""){
			alert("닉네임을 입력하세요");
			u_nickname.addClass("border-danger");
			u_nickname.focus();
			return false;
		}else{
			u_nickname.removeClass("border-danger");
			u_nickname.addClass("border-success");
		}
		
		if(!existCheckNickname){
			alert("닉네임 중복체크를 하세요");
			u_nickname.removeClass("border-success");
			u_nickname.addClass("border-danger");
			return false;
		}else{
			joinParams.u_nickname = u_nickname.val();
		}
		
		if(u_name.val().replace(/^\s*/, "") == null || u_name.val().replace(/^\s*/, "") == ""){
			alert("이름을 입력하세요");
			u_name.addClass("border-danger");
			u_name.focus();
			return false;
		}else{
			u_name.removeClass("border-danger");
			u_name.addClass("border-success");
			
			joinParams.u_name = u_name.val().replace(/^\s*/, "");
		}
		
		if(!emailRegex.test(u_email.val())){
			alert("이메일 형식이 올바르지 않습니다");
			u_email.addClass("border-danger");
			u_email.focus();
			return false;
		}else{
			u_email.removeClass("border-danger");
			u_email.addClass("border-success");
			
			joinParams.u_email = u_email.val().replace(/^\s*/, "");
		}
		
		if(u_birth.val() == null || u_birth.val() == ""){
			alert("생일을 입력하세요");
			u_birth.addClass("border-danger");
			u_birth.focus();
			return false;
		}else{
			u_birth.removeClass("border-danger");
			u_birth.addClass("border-success");
			
			joinParams.u_birth = u_birth.val();
		}
		
		if(u_gender_m.is(":checked")){
			joinParams.u_gender = u_gender_m.val();	
		}else{
			joinParams.u_gender = u_gender_w.val();
		}
		
		joinParams.u_phone = u_phone.val();
		joinParams.u_postcode = u_postcode.val();
		joinParams.u_address = u_address.val();
		joinParams.u_address_detail = u_address_detail.val();
		joinParams.u_address_extra = u_address_extra.val();
		
		$.ajax({
			url : "/user/joinProccess",
			data : params,
			type : "post",
			success : function(returnData){
				if(returnData > 0){
					alert("이미 존재하는 아이디 입니다");
					u_id.removeClass("border-success");
					u_id.addClass("border-danger");
					u_id.focus();
					return false;
				}else{
					alert("사용 가능한 아이디 입니다");
					u_id.removeClass("border-danger");
					u_id.addClass("border-success");
					existCheckId = true;
				}
			},
			error : function(error){
				console.log(error);
			}
		})
	})
	
	$("#btnIdExistCheck").on("click", function(){
		let u_id = $("input[name=u_id]");
		let params = {
			u_id : u_id.val().replace(/^\s*/, "")
		}
		$.ajax({
			url : "/user/isUserIdExist",
			data : params,
			type : "get",
			success : function(returnData){
				if(returnData > 0){
					alert("이미 존재하는 아이디 입니다");
					u_id.removeClass("border-success");
					u_id.addClass("border-danger");
					u_id.focus();
					return false;
				}else{
					alert("사용 가능한 아이디 입니다");
					u_id.removeClass("border-danger");
					u_id.addClass("border-success");
					existCheckId = true;
				}
			},
			error : function(error){
				console.log(error);
			}
		})
	})
	
	$("#btnNickNameExistCheck").on("click", function(){
		let u_nickname = $("input[name=u_nickname]");
		
		if(!nicknameRegex.test(u_nickname.val().replace(/^\s*/, ""))){
			alert("닉네임은 특수문자를 제외한 2~10자리를 입력하세요");
			u_nickname.addClass("border-danger");
			u_nickname.focus();
			return false;
		}
		
		let params = {
			u_nickname : u_nickname.val().replace(/^\s*/, "")
		}
		$.ajax({
			url : "/user/isUserNicknameExist",
			data : params,
			type : "get",
			success : function(returnData){
				if(returnData > 0){
					alert("이미 존재하는 닉네임 입니다");
					u_nickname.removeClass("border-success");
					u_nickname.addClass("border-danger");
					u_nickname.focus();
					return false;
				}else{
					alert("사용 가능한 닉네임 입니다");
					u_nickname.removeClass("border-danger");
					u_nickname.addClass("border-success");
					existCheckNickname = true;
				}
			},
			error : function(error){
				console.log(error);
			}
		})
	})
	
	$("input[name=u_postcode]").on("focus", function(){
		new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                u_postcode.value = data.zonecode;
                u_address.value = roadAddr;

            }
        }).open();
        
        u_postcode.blur();
	})
})