$(function () {
    // loadDiv(1);
    var goodsId = $("#goodsId").val();
    $("#"+goodsId).addClass("active");
    refsh();
    alert($("#"+goodsId).attr("class"));
});

// 动态加载预约div
function loadDiv(id) {
    var tenantId = 1;
    $.ajax({
        url: '/order/preSale/load',
        type: 'POST',
        data: {
            id: id,
            tenantId: tenantId
        },
        dataType: "html", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            $("#preSaleId").html(data);
            $("#"+id).addClass("active").siblings().removeClass("active");
            refsh();
        }
    });
}

function refsh() {
    // 颜色切换事件
    $(".color").find("i").each(function () {
        $(this).click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var id = $(this).attr("id");
            // loadDiv(id);
        })
    })
    // 数量减号事件
    $(".minus").click(function () {
        if ($(".num").val() >= 1) {
            $(".num").val(parseInt($(".num").val()) - 1);
        }
        if ($(".num").val() < 1) {
            $(".addShopcar").css("background", "#ebebeb");
        }

    })
    // 数量加号事件
    $(".add").click(function () {
        $(".num").val(parseInt($(".num").val()) + 1);
        if ($(".num").val() >= 1) {
            $(".addShopcar").css("background", "#03a9f4");
        }
    })
}

function goods(id,tenantId) {
    self.location.href="/order/preSale?id="+id+"&tenantId="+tenantId;
}