$(".personMessage").click(function () {
	$(this).addClass("addrActive").siblings().removeClass("addrActive").find(".right").hide();
	$(this).find(".right").show();
})
$(document).on("click",".delete",function(){
	$(this).parent().remove();
	if ($(".personMessage").length < 4) {}{
		$(".personMessage-1").show();
	}
})
$(".personMessage-1").click(function(){
			
	$(this).before('<div class="personMessage">'+
					'<p class="name"><i class="name-1">郑丽</i><img class="right" src="img/right_03.png"></p>'+
					'<p class="province">北京市朝阳区</p>'+
					'<p class="homeAddr">垈头翠城清源104号低商</p>'+
					'<p class="phone"><span class="ph">131****4432</span><span class="postcode">100124</span></p>'+
					'<p class="tax">税费：<I class="money">$70.00</I></p>'+
					'<span class="edit">编辑</span>'+
					'<span class="delete">删除</span>'+
					'</div>');

	if ($(".personMessage").length >= 4) {
		$(this).hide();
	}
	
})

$(".buySer").find('.checkB').each(function(){
	$(this).click(function(){
		$(this).addClass("checkBo").parent().siblings().find(".checkB").removeClass("checkBo");
	})
})


$(".inTe").click(function(){
	$("body").append('<div class="mask"></div>');
	$(".mask").css("height",$(document).height()).show();
	$(".alertArea").show();
})
$(".cancel").click(function(){
	$(".mask").hide();
	$(".alertArea").hide();
})