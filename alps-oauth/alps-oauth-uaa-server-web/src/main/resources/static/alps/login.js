
$(function() {
    validateRule();
	$('.imgcode').click(function() {
		var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
		$(".imgcode").attr("src", url);
	});
});

$.validator.setDefaults({
    submitHandler: function() {
		login();
    }
});

function login() {
	// document.getElementById("signupForm").submit();
	$.modal.loading($("#signupForm").data("loading"));
	var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var domain =   $("input[name='domain']").is(':checked') == true ?  $.common.trim($("input[name='domain']").val()) : '';
    $.ajax({
        type: "post",
        url: "login",
        data: {
            "username": username,
            "password": password,
            "domain": domain,
        },
        success: function(e) {
            if (e.code == 0) {
            	$.modal.msg(e.msg);
            	location.href = ctx +"index";
            } else {
            	$.modal.msg(e.msg);
            	$.modal.closeLoading();
            }
        }
    });
	
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}
