$(function () {
   $(".meFeedback").show();
   createCode();
})
 //产生验证码  
 var code ; //在全局定义验证码   
 function createCode(){  
     code = "";   
     var codeLength = 4;//验证码的长度  
     var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',  
     'S','T','U','V','W','X','Y','Z');//随机数  
     for(var i = 0; i < codeLength; i++) {//循环操作  
        var index = Math.floor(Math.random()*36);//取得随机数的索引（0~35）  
        code += random[index];//根据索引取得随机数加到code上  
     }  
    $("#code").html(code);//把code值赋给验证码  
}
//提交  
function addFeedBack(){
    	//判断邮箱和用户名
    	var backName=$("#backName").val();
    	var userEmail=$("#userEmail").val();
    	var description=$("#description").val();
        var code = $("#validateCode").val().toUpperCase(); //取得输入的验证码并转化为大写
        //执行程序
    	if(validateName(backName)==true && validateEmail(userEmail)==true && validateDescription(description)==true && validate(code)==true){
		    $.ajax({
		        type: "POST",
		        url: "/user/email/feedback",
		        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
		        data: {userName:backName,email:userEmail,content:description},
		        success: function (data) {
		            if (data.result.code == 0) {
		               layer.msg('操作成功', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
		            }
		        }
		    });
    	}          
}
//校验验证码  
function validate(code){  
    var inputCode = $("#validateCode").val().toUpperCase(); //取得输入的验证码并转化为大写     
    if(inputCode.length <= 0) { //若输入的验证码长度为0  
        layer.msg('验证码不能为空', {icon: 1, shade: [0.5, '#393D49'], time: 1000}); //则弹出请输入验证码  
        return false;
    }else if(inputCode != code ) { //若输入的验证码与产生的验证码不一致时  
        layer.msg('验证码不正确', {icon: 1, shade: [0.5, '#393D49'], time: 1000}); //则弹出请输入验证码
        createCode();//刷新验证码  
        $("#validateCode").val("");//清空文本框  
        return false;
    }else { //输入正确时  
    	return true;
    }             
}
//验证用户名
function validateName(backName){
   if(backName==""){
	   layer.tips('姓名不能为空', '#backName',{time: 1500});
	   return false;
   }
   return true;
}
//验证内容
function validateDescription(description){
   if(description==""){
	   layer.tips('反馈意见不能为空', '#description',{time: 1500});
	   return false;
   }
   return true;
}
//验证邮箱
function validateEmail(userName){
   var reregex = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
   if(userName==""){
	   layer.tips('邮箱不能为空', '#userEmail',{time: 1500});
	   return false;
   }
   if(!reregex.test(userName)){
	   layer.tips('邮箱格式不正确', '#userEmail',{time: 1500});
	   return false;
   }
   return true;
}

