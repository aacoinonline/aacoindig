$(function () {
    // 自动上传图片
    $("#uploadFile").live('change', function () {
        // 获取img的src值
        var src = $('#imgFile').attr('src');
        var formData = new FormData($("#formId")[0]);
        $.ajax({
            url: '/user/headImg/upload',
            type: 'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
           dataType: "json", //返回的数据格式：json/xmlml/script/jsonp/text
           success: function (data) {
           	    if(data.result==1){
           	    	  layer.msg('文件上传不能大于3M', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
           	    	  $("#imgFile").attr("src", data.heading);
           	    }else{
                    $("#imgFile").attr("src", data.result);
           	    }
            }
        });
    })
    //打开修改页面
    $(".myXG").click(function(){
        $("body").append('<div class="mask"></div>');
        $(".mask").css("height",$(document).height()).show();
        $(".alertArea").show();
    })
   //关闭修改页面
    $(".cancel").click(function(){
        $(".mask").hide();
        $(".alertArea,.alertPassword,.alertEmail,.alertEmail2,.alertEmail3,.alertPhonel,.alertPhone2,.alertPhone3").hide();
    })
    //密码修改打开
    $(".changeMM").click(function(){
        $("body").append('<div class="mask"></div>');
        $(".mask").css("height",$(document).height()).show();
        $(".alertPassword").show();
    })
    //修改邮箱打开，以及下一步
     $(".changeEmail").click(function(){
        $("body").append('<div class="mask"></div>');
        $(".mask").css("height",$(document).height()).show();
        $(".alertEmail").show();
        //页面初始化时，焦点定位第一个文本框内
        $("#emailNumber").val("");
	    $('#step1 input').focus();
    })
    //绑定取消事件
     $(".quxiao").click(function(){
        $(".mask").hide();
        $(".alertEmail2,.alertPhone2").hide();
    })
    //邮箱绑定完成事件
    $("#doneBtn").click(function(){
        $(".mask").hide();
        $(".alertEmail3").hide();
        //刷新页面
        window.location.reload();
    })
    //手机绑定完成关闭
     $("#donePhoneBtn").click(function(){
        $(".mask").hide();
        $(".alertPhone3").hide();
        //刷新页面
        window.location.reload();
    })
    $(".phoneBang").click(function(){
        $("body").append('<div class="mask"></div>');
        $(".mask").css("height",$(document).height()).show();
        $(".alertPhonel").show();
        //页面初始化时，焦点定位第一个文本框内
        $("#phoneNumber").val("");
	    $('#stepPhone1 input').focus();
    })
    //发送手机验证码倒计时(只有中国才有不要国际化)
	$("#reptSend").click (function  () {
	    var validCode=true;
		var time=60;
		var code=$(this);
		var phoneNumber=$("#phoneNumber").val();
		if (validCode) {
			   $.ajax({
			        type: "POST",
			        url: "/user/send/bind/code",
			        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
			        data: {account:phoneNumber},
			        success: function (data) {
			            if (data.result.code == 0) {
			                validCode=false;
			                code.addClass("msgs1");
			                $('#stepPhone2 input').focus();
			                var t=setInterval(function  () {
							time--;
							code.html(time+"秒");
							if (time==0) {
								clearInterval(t);
							    code.html("重新获取");
								validCode=true;
							    code.removeClass("msgs1");
							}},1000)}}
                   });
		  }
	})   
	//发送邮件验证码倒计时(不需要国际化)
	$("#reptSendEmail").click (function  () {
	    var validCode=true;
		var time=60;
		var code=$(this);
		var emailNumber=$("#emailNumber").val();
		if (validCode) {
			   $.ajax({
			        type: "POST",
			        url: "/user/send/bind/code",
			        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
			        data: {account:emailNumber},
			        success: function (data) {
			            if (data.result.code == 0) {
			                validCode=false;
			                code.addClass("msgs1");
			                $('#step2 input').focus();
			                var t=setInterval(function  () {
							time--;
							code.html(time+"秒");
							if (time==0) {
								clearInterval(t);
							    code.html("重新获取");
								validCode=true;
							    code.removeClass("msgs1");
							}},1000)}}
                  });
		  }
	}) 
})

//加载用户信息
function initUser() {
    $.ajax({
        type: "POST",
        url: "/user/userInfo/list",
        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {},
        success: function (data) {
            if (data.result.code == 0) {
                $("#nickName").val(data.result.value.userDetailEntity.nickName);
                $("input[type='radio'][name='sex'][value='" + data.result.value.userDetailEntity.sex + "']").attr("checked", "checked");
                if (data.result.value.userDetailEntity.birth != "") {
                    var data = data.result.value.userDetailEntity.birth.split("-");
                    $("#year").val(data[0]);
                    $("#month").val(data[1]);
                    $("#day").val(data[2]);
                }
            }
        }
    });
}
//修改用户数据
function addUserInfo() {
    var nickName = $("#nickName").val();
    var sex = $('input[name="sex"]:checked').val();
    var year = $("#year").find("option:selected").text();
    var month = $("#month").find("option:selected").text();
    var day = $("#day").find("option:selected").text();
    var birth = year + "-" + month + "-" + day;
    $.ajax({
        type: "POST",
        url: "/user/update/userInfo",
        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {
            nickName: nickName,
            sex: sex,
            birth: birth
        },
        success: function (data) {
            if (data.result.code == 0) {
                layer.msg('操作成功', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
                $("#nick").html(nickName);
                $("#birthday").html(birth);
                if (sex==0){
                    $(".nature").html("女");
                }else if(sex==1){
                    $(".nature").html("男");
                }else{
                    $(".nature").html("保密");
                }
                $(".mask").hide();
                $(".alertArea").hide();
            }
        }
    });
}
//修改用户密码
function updatePass(){
	var oldPass=$("#oldPass").val();
	var newPass=$("#newPass").val();
	var reptPass=$("#reptPass").val();
	if(checkPass(oldPass)==true && checkReptPass(newPass,reptPass)==true){
	  $.ajax({
		        type: "POST",
		        url: "/user/update/pass",
		        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
		        data: {
		             oldPass:Base64.encode(oldPass),
		             newPass:Base64.encode(newPass)
		          },
		        success: function (data) {
		            if (data.result.code == 0) {
		               layer.msg('操作成功', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
		               //关闭弹出层
		               $("#alertPassword").hide();
		               window.location.href="/user/login";
		            }
		             if (data.result.code == 2006) {
		               layer.tips('密码不正确', '#oldPass',{time: 1500});
		            }
		        }
		  });
	}
}
//绑定账号发送验证码(只有中国实用不需要国际化)type=1邮箱2手机
function sendPhoneCode(type){
	var phoneNumber;
    if(type==1){
        phoneNumber=$("#emailNumber").val();
    }
    if(type==2){
        phoneNumber=$("#phoneNumber").val();
    }
  if(checkPhone(phoneNumber,type)==true){
  	 $.ajax({
        type: "POST",
        url: "/user/send/bind/code",
        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {account:phoneNumber},
        success: function (data) {
            if (data.result.code == 0) {
            	 if(type==1){
            	 	  $("#step1").hide();
	                  $("#step2").show();
	                  $("#emailSayIt").html("我们向 "+ phoneNumber+" 发送了验证邮件请输入邮件中的验证码"); 
	                  $('#step2 input').focus();
            	    }
            	 if(type==2){
	                  $("#stepPhone1").hide();
	                  $("#stepPhone2").show();
	                  $("#phoneSayIt").html("我们向 ＋86 "+ phoneNumber+" 发送了一条验证短信请输入短信中的验证码"); 
	                  $('#stepPhone2 input').focus();
            	   }
            }
           if(data.result.code==2002){
           	   if(type==1){
           	      layer.tips('账号已被绑定', '#emailNumber',{time: 1500});
           	   }
           	   if(type==2){
           	      layer.tips('账号已被绑定', '#phoneNumber',{time: 1500});
           	   }
           } 
       }
    });
  }
}
//账号绑定手机(只有中国实用不需要国际化)
function bindPhone(type){
	var phoneNumber,phoneCode;
	 if(type==1){
	 	 phoneNumber=$("#emailNumber").val();
	     phoneCode=$("#emailCode").val();
	 } 
	 if(type==2){
	     phoneNumber=$("#phoneNumber").val();
	     phoneCode=$("#phoneCode").val();
	 }
	 $.ajax({
        type: "POST",
        url: "/user/bind/account",
        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {account:phoneNumber,code:phoneCode},
        success: function (data) {
            if (data.result.code == 0) {
            	if(type==1){
            	    $("#step2").hide();
	                $("#step3").show();
            	}
            	if(type==2){
	                $("#stepPhone2").hide();
	                $("#stepPhone3").show();
            	}
           }
            if (data.result.code == 2005) {
               layer.msg('验证码不正确', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
           }
       }
    });
}
//验证手机号格式(不需要国际化)
function checkPhone(phoneNumber,type){
	//验证邮箱
    if(type==1){
  	   var reregex = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	   if(!reregex.test(phoneNumber)){
		   layer.tips('邮箱格式不正确', '#emailNumber',{time: 1500});
		   return false;
	   }
    } 
    if(type==2){
	   var reregex = /^1[3|4|5|6|7|8][0-9]{9}$/;
	   if(!reregex.test(phoneNumber)){
		   layer.tips('手机格式不正确', '#phoneNumber',{time: 1500});
		   return false;
	   }
  }
   return true;
}
//验证密码
function checkPass(oldPass){
  if(oldPass==""){
	   layer.tips('密码不能为空', '#oldPass',{time: 1500});
	   return false;
   }
   return true;
}
//验证输入密码
function checkReptPass(newPass,reptPass){
   if(newPass==""){
	   layer.tips('密码不能为空', '#newPass',{time: 1500});
	   return false;
   }
    if(newPass.length<6){
	   layer.tips('密码长度不能少于6位', '#newPass',{time: 1500});
	   return false;
   }
     if(newPass!=reptPass){
	   layer.tips('密码输入不一致', '#reptPass',{time: 1500});
	   return false;
   }
   return true;
} 
