$(function () {
    var status;
    loadOrderList(status);
});

/**
 * 动态加载订单列表
 */
function loadOrderList(param) {
    // 暂时默认，后期会从页面取值
    var tenantId = 1;
    var userId = 1;
    $.ajax({
        url: '/order/list/load',
        type: 'POST',
        data: {
            status:param
        },
        dataType: "html", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            $("#addTr").html(data);
            // 快递公司编码
            var way;
            // 物流单号
            var logisticCode;
            $(".logisticsId").mouseover(function(){
                var str = $(this).attr("value").split(",");
                way = str[0];
                logisticCode = str[1];
                $.ajax({
                    type: "POST",
                    url: "/order/logistics",
                    dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
                    async: false,
                    data: {
                        way:way,
                        logisticCode:logisticCode
                    },
                    success: function (data) {
                        // $("#"+way+logisticCode).empty();
                        // $(this).siblings("div").html(data);
                        $("#"+way+logisticCode).html(data);
                    }
                });
                $("#"+way+logisticCode).show();
                // $(this).siblings("div").show();
            });
            $(".logisticsId").mouseout(function(){

                window.timer=setTimeout(function(){
                    // $("#"+way+logisticCode).hide();
                    $("#"+way+logisticCode).hide();
                },500)
            });
            $(".logistics-pop").mouseover(function(){
                clearTimeout(window.timer);
                $.ajax({
                    type: "POST",
                    url: "/order/logistics",
                    dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
                    async: false,
                    data: {
                        way:way,
                        logisticCode:logisticCode
                    },
                    success: function (data) {
                        // $("#"+way+logisticCode).empty();
                        // $(this).html(data);
                        $("#"+way+logisticCode).html(data);
                    }
                });
                $("#"+way+logisticCode).show();
            });
            $(".logistics-pop").mouseout(function(){
                $("#"+way+logisticCode).hide();
            });
        },
        error: function (data) {
            alert("error" + data);
        }
    });
}

/**
 * 状态查询
 * @param param 订单状态
 */
function getOrderStatus(param) {
    if(param == 1001) {
        loadOrderList(param);
    } else if (param == 5001) {
        loadOrderList(param);
    }else {
        loadOrderList(param);
    }
}

/**
 * 取消订单
 * @param orderNo 订单号
 */
function updateOrderStatus(orderNo) {
    var param;
    $.ajax({
        url: '/order/cancel',
        type: 'POST',
        data: {
            orderNo: orderNo
        },
        dataType: "json", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            if(data.result == 1) {
                loadOrderList(param);
            }
        },
        error: function (data) {
            alert("error" + data);
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
            $("#divId").empty();
            $("#divId").html(data);
        }
    });
}