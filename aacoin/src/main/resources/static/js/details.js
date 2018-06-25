if ($(".num").val() >0) {
	$(".addShopcar").css("background","#03a9f4");
}
$(".productTop").find("p").each(function(){
	$(this).click(function(){
		$(this).addClass("active").siblings().removeClass("active");
	})
})

$(".color").find("i").each(function(){
	$(this).click(function(){
		$(this).addClass("active").siblings().removeClass("active");
	})
})
$(".minus").click(function(){
	if ($(".num").val() >= 1) {
		$(".num").val(parseInt($(".num").val()) - 1);
	}
	if ($(".num").val() <1) {
		$(".addShopcar").css("background","#ebebeb");
	}
	
})
$(".add").click(function(){
	$(".num").val(parseInt($(".num").val()) + 1);
	if ($(".num").val() >=1) {
		$(".addShopcar").css("background","#03a9f4");
	}
})