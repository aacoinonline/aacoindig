$(function () {
    myAddress();
    countPrice();
    //国家省市县级联和切换效果
    var conuty = $("#conuty");
    var province = $('#province');
    var city = $('#city');
    //省级联市效果
    province.unbind("change").change(function () {
        var parentId = $(this).val();
        $("#city").find("option:gt(0)").remove();// 清空市
        $("#conuty").find("option:gt(0)").remove();// 清空县
        if (parentId != 0) {
            $.post("/user/userAddress/conuty/children", {
                parentId: parentId
            }, function (data) {
                for (var i = 0; i < data.result.value.length; i++) {
                    var str = data.result.value[i];
                    city.append("<option value='" + str.id + "'>" + str.foreignName + "</option>");
                }
            }, "json");
        }
    });
    //市级联区效果
    city.unbind("change").change(function () {
        var parentId = $(this).val();
        $("#conuty").find("option:gt(0)").remove();// 清空县
        if (parentId != 0) {
            $.post("/user/userAddress/conuty/children", {
                parentId: parentId
            }, function (data) {
                for (var i = 0; i < data.result.value.length; i++) {
                    var str = data.result.value[i];
                    conuty.append("<option value='" + str.id + "'>" + str.foreignName + "</option>");
                }
            }, "json");
        }
    });

    // 地址选中加载
    $(".address ul li").live('click', function () {
        $(".address ul li").each(function () {
            $(this).removeClass("checked");
        })
        $(this).addClass("checked");
        var userinfo = $(this).attr("id");
        $("#userinfo").val(userinfo);
        $("#shouhuodizhi").val("");
    });

    //关闭弹出层
    $(".close_pop").click(
        function () {
            $("#popup").hide();
        });
});

//我的地址列表
function myAddress() {
    $.ajax({
        type: "POST",
        url: "/order/address/list",
        dataType: 'html',  //返回的数据格式：json/xmlml/script/jsonp/text
        data: {
            userId: 1,
            page: 1,
            size: 10
        },
        success: function (data) {
            $("#userAddressList").empty();
            $("#userAddressList").html(data);
        }
    });
}

/**
 * 加密手机号
 * @param mobile 手机号
 * @returns {string}
 */
function getPhone(mobile) {
    var mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
    return mobile;
}

//增加时候初始化省级数据
function initProvince() {
    var province = $("#province");
    province.append("<option value='0'>省</option>");
    if (province.find("option").length <= 1) {
        $.ajax({
            url: "/user/userAddress/conuty/children",
            dataType: 'json',
            type: 'post',
            async: false,
            data: {parentId: 1},
            success: function (data) {
                for (var i = 0; i < data.result.value.length; i++) {
                    var str = data.result.value[i];
                    province.append("<option value='" + str.id + "'>" + str.foreignName + "</option>");
                }
            }
        })
    }
}

//添加or修改收货地址
function insertAddress() {
    var submitType = $("#submitType").val();
    var countryId = 1;//中国
    var provinceId = $("#province").find("option:selected").val();
    var cityId = $("#city").find("option:selected").val();
    var conutyId = $("#county").find("option:selected").val();
    var firstName = $("#firstName").val();
    var familyName = $("#familyName").val();
    var mobile = $("#mobile1").val();
    var postCode = $("#postCode").val();
    var detail = $("#detail").val();
    var addressId = $("#addressId").val();
    var url;
    if (submitType == "add") {
        url = "/user/userAddress/add";
    }
    if (submitType == "update") {
        url = "/user/userAddress/update";
    }
    if (findName(firstName, familyName) == true && findPhone(mobile) == true && findCounty() == true && findPostCode(postCode) && findAddress(detail) == true) {
        //提交表单数据
        $.ajax({
            type: "POST",
            url: url,
            dataType: 'json',  //返回的数据格式：json/xmlml/script/jsonp/text
            data: {
                userId: 1,
                firstName: firstName,
                familyName: familyName,
                mobile1: mobile,
                postCode: postCode,
                detail: detail,
                countryId: countryId,
                provinceId: provinceId,
                cityId: cityId,
                conutyId: conutyId,
                id: addressId
            },
            success: function (data) {
                if (data.result.code == 0) {
                    $("#popup").hide();
                    layer.msg('操作成功', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
                    myAddress();
                } else {
                    layer.msg(data.result.message, {icon: 1, shade: [0.5, '#393D49'], time: 1000});
                }
            }
        });
    }

}

/**
 * 保存gift信息
 */
function saveGift(param) {
    var param = "gift" + param;
    $("#" + param).attr("disabled", "disabled");
}

/**
 * 取消gift信息
 */
function resetGift(param) {
    var param = "gift" + param;
    $("#" + param).val("");
}

/**
 * 生成订单
 */
function submitOrder() {
    // 遍历商品列表
    var urls = "";
    var money = 0;
    var count = 0;
    var input = $("input[name='details']");
    for (var i = 0; i < input.length; i++) {
        if ("" != urls) {
            urls += ",";
        }
        urls += input[i].value;
        var id = input[i].id;
        id = id.substring(7, id.length);
        var str = $("#gift" + id).val();
        urls += "#" + str;
    }
    // 收货地址
    var address = $("#shouhuodizhi").val();
    if (null != address && "" != address) {
        $("#userinfo").val(address);
    }
    if (null == $("#userinfo").val() || "" == $("#userinfo").val()) {
        layer.msg('请选择收货地址！', {icon: 1, shade: [0.5, '#393D49'], time: 1000});
        return;
    }
    // 商品总金额
    var goodsPrice = $("#goodsCountPrice").text().split("$");
    $("#originalPrice").val(goodsPrice[1])
    // 商品详情
    $("#orderDetails").val(urls);
    // 订单总价
    var price = $("#priceId").text().split("$");
    $("#receiveMoney").val(price[1]);
    var formData = $("#formId").serialize();
    $.ajax({
        url: '/order/add/order',
        type: 'POST',
        data: formData,
        dataType: "json", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            if (data.result != null) {
                if (data.result == 0) {
                    // payment(data.orderNo);
                    self.location.href = "/order/back/order?pay=" + data.orderNo;
                }
            }
        },
        error: function (data) {
            alert("error" + data);
        }
    });
}

/**
 * 跳到待支付页面
 * @param orderNo 订单号
 */
function payment(orderNo) {
    $.ajax({
        url: '/order/add/order',
        type: 'POST',
        data: formData,
        dataType: "json", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            if (data.result != null) {
                if (data.result == 0) {
                }
            }
        },
        error: function (data) {
            alert("error" + data);
        }
    });
}

//打开增加页面
function openAddress() {
    $("#submitType").val("add");
    $("#addressType").html("增加收货地址");
    //先清空值
    $(".p-ibox input[type='text']").val("");
    $("#province").find("option").remove();// 清空市
    $("#city").find("option:gt(0)").remove();// 清空市
    $("#conuty").find("option:gt(0)").remove();// 清空县
    $("#resetId").click();
    $("#popup").show();
    //加载省级数据
    initProvince();
}

//验证姓名
function findName(firstName, familyName) {
    if (firstName == "" || familyName == "") {
        $("#prompt").html("姓名不能为空");
        return false;
    }
    return true;
}
//验证邮编
function findPostCode(postCode) {
    if (postCode == "") {
        $("#prompt").html("邮编不能为空");
        return false;
    }
    return true;
}
//验证详细地址
function findAddress(address) {
    if (address == "") {
        $("#prompt").html("详细地址不能为空");
        return false;
    }
    return true;
}
//验证手机号码
function findPhone(mobile) {
    var patrn = /^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$/;// 手机正则表达式
    if (!patrn.test(mobile)) {
        $("#prompt").html("请输入正确手机号码");
        return false;
    }
    return true;
}
//验证省市县
function findCounty() {
    if ($("#province").find("option:selected").val() != 0 && $("#city").find("option:selected").val() != 0 && $("#county").find("option:selected").val() != 0) {
        return true;
    } else {
        $("#prompt").html("请选择省市县信息");
        return false;
    }
}

function countPrice() {
    var goodsCountPrice = $("#goodsCountPrice").text().split("$");
    var taxationId = $("#taxationId").text().split("$");
    var freightId = $("#freightId").text().split("$");
    var countPrice = Number(goodsCountPrice[1]) + Number(taxationId[1]) + Number(freightId[1]);
    var reg = /.*\..*/;
    if (reg.test(countPrice)) {
        $("#priceId").text("$"+countPrice);
    } else {
        var price = countPrice + ".00";
        $("#priceId").text("$"+price);
    }
}