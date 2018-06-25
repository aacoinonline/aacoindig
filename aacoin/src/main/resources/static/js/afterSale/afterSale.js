$(function (){
	over();
	//查看申请售后服务列表
	$("#applyService").click(function(){
		over();
	});
})
//已处理列表
function disposedService(){
	$("#applyService").removeClass("tableSwitch");
	$("#disposedService").addClass("tableSwitch");
	$.ajax({
        type: "POST",
        url: "/afterSale/dispose/list",
        dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {
        	userId:"1",
        	pageNum:1,
        	pageSize:10
        },
        success: function (data) {
         $("#apply").empty();
         $("#apply").html(data);
        }
    });
}
//初始化加载数据
function over(){
	$("#applyService").addClass("tableSwitch");
	$("#disposedService").removeClass("tableSwitch");
	$.ajax({
        type: "POST",
        url: "/afterSale/apply/list",
        dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {
        	userId:"1",
        	pageNum:1,
        	pageSize:10
        },
	   success: function (data) {
         $("#apply").empty();
         $("#apply").html(data);
        }
    });
}


 //处理申请流程
 function applyInfo(orderNo,goodsId,orderId){
	$("#disposedService").removeClass("tableSwitch");
	$("#applyService").addClass("tableSwitch");
	$.ajax({
        type: "POST",
        url: "/afterSale/apply/detail",
        dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {
        	orderNo:orderNo,
        	goodsId:goodsId,
        	orderId:orderId
        },
	   success: function (data) {
         $("#apply").empty();
        $("#apply").html(data);
        }
    });
}
 //提交表单
 function insertApply(){
	 var orderNo=$("#orderNo").val();
	 var goodsId=$("#goodsId").val();
	 var orderCount=$("#orderCount").val();
	 var applyTypes=$("#applyTypes").val();
	 var goodsName=$("#goodsName").html();
	 var reason=$("#reason").find("option:selected").text();
	 var logisticsNo=$("#logisticsNo").val();
	 var consignee=$("#consignee").val();
	 var receivePhone=$("#receivePhone").val();
	 var receiveAddress=$("#receiveAddress").val();
	 var goodsCount=$("#goodsCount").val();
	 var logisticsName=$("#logisticsName").val();
	 var deploy=$("#deploy").html();
	 var goodsImg=$("#goodsImg").val();
	 $.ajax({
	        type: "POST",
	        url: "/afterSale/apply/insert",
	        dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
	        data: {
	        	orderNo:orderNo,
	        	goodsId:goodsId,
	        	consignee:consignee,
	        	orderCount:orderCount,
	        	applyTypes:applyTypes,
	        	goodsName:goodsName,
	        	reason:reason,
	        	logisticsNo:logisticsNo,
	        	goodsCount:goodsCount,
	        	receivePhone:receivePhone,
	        	receiveAddress:receiveAddress,
	        	logisticsName:logisticsName,
	        	deploy:deploy,
	        	goodsImg:goodsImg
	        },
		   success: function (data) {
			   if(data.code==0){
				   layer.msg('提交成功',{icon: 1,shade: [0.5, '#393D49'],time:1000});
				   //刷新
				   disposedService();
			   }else{
				   layer.msg(data.message,{icon: 1,shade: [0.5, '#393D49'],time:1000});
			   }
	        }
	    });
 }
	