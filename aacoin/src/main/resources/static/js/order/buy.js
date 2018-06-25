$(function () {
    loadDiv(1);
    $(".list1 ul li").live('click', function () {
        $(".list1 ul li").each(function () {
            $(this).click(function () {
                $(".list1 ul li").removeClass("touch");
            })
        })
        $(this).addClass("touch");
        var goodsId = $(this).attr("id");
        $("#goodsId").val(goodsId);
    });
    $(".list3 ul li").live('click', function () {
        if ($(this).attr("class") != "undefined" && $(this).attr("class") != "dash") {
            $(".choseNum li").removeClass("touch1");
            $(this).addClass("touch1");
            var count = $(this).attr("id");
            $("#countId").val(count);
        }
    });

    $(".thumblist li a").live('click', function () {
        $(".jqzoom").attr('src',$(this).find("img").attr("mid"));
    });

});


/**
 * 立即购买
 */
function submit() {
    $("#formId").submit();
}

/**
 * 选择配置
 * @param id 商品id
 */
function selectedDeploy(id) {
    var tenantId = 1;
    loadDiv(id);
    $(".choseOne li").each(function () {
        $(this).remove();
    })
}

/**
 * 动态加载商品购买页面
 * @param id 商品id
 */
function loadDiv(id) {
    var tenantId = 1;
    $.ajax({
        url: '/order/buy/load',
        type: 'POST',
        data: {
            id: id,
            tenantId: tenantId
        },
        dataType: "html", //返回的数据格式：json/xmlml/script/jsonp/text
        success: function (data) {
            $("#addDiv").html(data);
            $("#" + id).attr("class", "touch");
            // 初始化省级数据
            initProvince();
            // 切换省市级联动效果
            provinceAndCity();
        }
    });
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

/**
 * 切换省市级联动效果
 */
function provinceAndCity() {
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
}

// 查询税费
function findRates() {
    var country = "usa";
    var zipCode = "90001";
    var price = 1000;
    $.ajax({
        url: "/order/rates",
        type: 'post',
        data: {
            country:country,
            zipCode:zipCode,
            price:price
        },
        dataType: 'json',
        success: function (data) {
            alert(1);
        },error: function (data) {
            alert("error" + data);
        }
    })
}

function addShoppingCar() {
    alert("加入购物车");
}