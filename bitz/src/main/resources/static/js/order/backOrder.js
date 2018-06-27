$(document).ready(function() {
    // 获取倒计时时间
    var times = $("#surplusDate").val(); // 以秒为单位
    countTime = setInterval(function() {
        times = --times < 0 ? 0 : times;
        var h=Math.floor(times/60/60%24).toString();
        if(h.length <= 1) {
            h = "0" + h;
        }
        var m=Math.floor(times/60%60).toString();
        if(m.length <= 1) {
            m = "0" + m;
        }
        var s=Math.floor(times%60).toString();
        if(s.length <= 1) {
            s = "0" + s;
        }
        if(times == 0) {
            if(h == 00 && m == 00 && s==00) {
                $.ajax({
                    url: '/order/cancel',
                    type: 'POST',
                    data: {
                        orderNo: $("#orderNo").val()
                    },
                    dataType: "json", //返回的数据格式：json/xmlml/script/jsonp/text
                    success: function (data) {
                        if(data.result == 0) {
                            self.location.href = "/order/list";
                        }
                    }
                });
            }
            clearInterval(countTime);
        }
        // 获取分钟、毫秒数
        var string="<span>"+h+"</span> : <span>"+m+"</span> : <span>"+s+"</span>";
        $("#countdown").html(string);
    }, 1000);
});

/**
 * 立即支付
 */
function pay() {
    $("#payForm").submit();
}