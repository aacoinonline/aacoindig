package com.aacoin.dig.api.resource;

import com.aacoin.dig.BaseResource;
import com.aacoin.dig.Constant;
import com.aacoin.dig.Response;
import com.aacoin.dig.Result;
import com.aacoin.dig.api.entity.Accounts;
import com.aacoin.dig.api.entity.AccountsData;
import com.aacoin.dig.api.entity.CurrentOrders;
import com.aacoin.dig.api.entity.OrderDetail;
import com.aacoin.dig.api.service.AacoinService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;

@Path("/aacoin")
@Api(value = "/aacoin", description = "aacoinSource")
public class AacoinSource extends BaseResource {

    private static final Logger log = LoggerFactory.getLogger(AacoinSource.class);

    @Autowired
    private AacoinService aacoinService;


    //每次交易最小的Eth数量
    private static final Double MINI_AMOUNT_ETH = 0.0025;


    @ApiOperation(value = "/order/place/buy-market", notes = "下单 buy-market：市价买入")
    @Path("/order/place/buy-market")
    @POST
    @Produces({Constant.APPLICATION_JSON_UTF8})
    @Consumes({Constant.APPLICATION_JSON_UTF8})
    public Response orderPlace_buyMarket(@QueryParam("secretKey") String secretKey, @QueryParam("accessKey") String accessKey,
                                         @DefaultValue("AAT_ETH") @QueryParam("symbol") String symbol, @DefaultValue("500.0") @QueryParam("sellCoinCount") Double sellCoinCount,
                                         @DefaultValue("10") @QueryParam("digCount") Integer digCount) throws IOException, InterruptedException {
        Response<String> response = null;
        Double addRedPrice = 0.0;
        String returnResult = null;
        Double buyPrice = null;         //购买的价格
        Boolean isEthAvailable = false; //钱包中的Eth是否可用,小于0.002不可用
        Double ethAmount = null;        //账户中eth的数量
        String quantity = null;         //购买的数量
        String type = null;
        Boolean isMakeOrder = false;
        String currentOrders = null; //查询当前的订单
        Integer orderTotal = 0;
        Integer cancelOrderCount = 0;
        Double sellPrice = null;         //卖出的价格
        Double sellCount = null;         //卖出的数量
        Boolean isCoinAvailable = true; //钱包中的代币总量是否可用,小于0.002不可用

        for (int j = 0; j < digCount; j++) {
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||挖矿次数 ：：：第 " + j + "次");
            //用eth买入对应的币
            Integer addPrice = 1;
            while (true) {
                System.out.println("=================================================================================");
                addPrice = addPrice + 1;
                type = "buy-limit"; //TODO 买卖分类
                //TODO 1.获取所有的账户信息,得到账户中含有的虚拟币的数量（以交易币和Eth为主）
                List<AccountsData> accountsDataList = aacoinService.getAccountCoins(secretKey, accessKey);
                System.out.println("账户信息 ：");
                for (AccountsData accountsData : accountsDataList) {
                    System.out.println(accountsData.getCurrencyCode() + " :  " + accountsData.getAccounts().get(0).getBalance());
                }

                //TODO 2.获取当前成交价格进行
                String currentPrice = aacoinService.getCurrentPrice(symbol);
                Double dCurrentPrice = Double.valueOf(currentPrice) + addPrice * addRedPrice;
                System.out.println(symbol + " 当前交易价格 : " + currentPrice);
                System.out.println(symbol + " 购买价格 : " + dCurrentPrice.toString());
                for (AccountsData accountsData : accountsDataList) {
                    //获取账户中eth的数量
                    if (accountsData.getCurrencyCode().equals("ETH")) {
                        List<Accounts> accountses = accountsData.getAccounts();
                        for (Accounts accounts1 : accountses) {
                            ethAmount = Double.valueOf(accounts1.getBalance());
                            System.out.println(" 可用Eth数量 : " + ethAmount);
                            if (ethAmount > MINI_AMOUNT_ETH && ethAmount > dCurrentPrice) { //Eth的数量大于当前购买的价格并且大于最小交易额0.0025
                                System.out.println(" 可以下单交易 : ");
                                isEthAvailable = true;
                            } else {
                                System.out.println(" 不需要下单交易 : ");
                                isEthAvailable = false;
                            }
                        }
                    }
                }

                //有Eth的情况下执行下单的操作
                if (isEthAvailable) {
                    Double enableBuyCount = ethAmount / dCurrentPrice; //可以购买的次数
                    if (enableBuyCount > 0) {
                        //TODO 3.下单
                        System.out.println("开始下单：" + "购买价格 : " + dCurrentPrice.toString());
                        isMakeOrder = aacoinService.makeOrder(secretKey, accessKey, symbol, type, enableBuyCount.toString(), dCurrentPrice.toString());
                        if (!isMakeOrder) {
                            System.out.println("下单失败");
                            break;//下单失败直接返回
                        }
                    }
                }

                //TODO 4.延迟3秒确认
                Thread.sleep(3000);

                //TODO 5.order/currentOrders 获取当前委托订单
                //symbol 交易市场（例：BCC_ETH）
                currentOrders = aacoinService.currentOrders(secretKey, accessKey, symbol, type, "1", "10000");

                Gson gson = new Gson();
                OrderDetail orderDetail = gson.fromJson(currentOrders, OrderDetail.class);
                orderTotal = orderDetail.getData().getTotal();
                System.out.println("订单总数量 ： " + orderTotal);
                if (orderTotal > 0) {
                    List<CurrentOrders> ordersList = orderDetail.getData().getList();
                    for (CurrentOrders order : ordersList) {
                        String status = order.getStatus();
                        //如果订单处于：尚未成交或者部分成交的状态，则取消订单
//TODO 订单状态没有返回Api-Bug
//                        if (status.equals("open") || status.equals("partial_filled")) {
                        Double orQuantity = Double.valueOf(order.getQuantity());
//                        Integer quantityInt = Integer.valueOf(order.getQuantity());
                        //当委托数量 大于0的情况下，即为没有完全成交，撤销订单
                        if (orQuantity.intValue() > 0) {
                            //取消订单
                            String orderId = order.getOrderId();
                            ///order/cancel 撤单
                            Boolean cancelOrder = aacoinService.orderCancel(secretKey, accessKey, orderId);
                            if (cancelOrder) {
                                cancelOrderCount = cancelOrderCount + 1;
                                System.out.println("订单撤销成功 ：：： " + "订单号：：： " + orderId);
                            }
                        }
                        System.out.println("撤销订单总数量 ; " + cancelOrderCount);
                        System.out.println("              ");
                    }
                }
                if (orderTotal == 0) {
                    break; //推出当前循环
                }
                System.out.println("=================================================================================");
            }

            Double sellForEthCount = 0.0;
            Double sellForEthAmount = 0.0;
            //卖出对应的币获得eth
            Integer reducePrice = 1;
            while (true) {
                System.out.println("********************************************************************************");
                type = "sell-limit"; //                type = "sell-limit";
                String sellCoinArr[] = symbol.split("_");
                String sellCoin = sellCoinArr[0];

                //TODO 1.获取所有的账户信息,得到账户中含有的虚拟币的数量（以交易币和Eth为主）
                List<AccountsData> accountsDataList = aacoinService.getAccountCoins(secretKey, accessKey);
                for (AccountsData accountsData : accountsDataList) {
                    System.out.println(accountsData.getCurrencyCode() + " :  " + accountsData.getAccounts().get(0).getBalance());
                    if (accountsData.getCurrencyCode().equals(sellCoin)) {
                        Double coinCount = Double.valueOf(accountsData.getAccounts().get(0).getBalance());
                        sellForEthCount = coinCount; //账户中含有的可以交易的币的数量
                        if (sellForEthCount > sellCoinCount) {
                            sellForEthCount = sellCoinCount; //按设定的数量卖出
                        } else {
                            return new Response(Result.SUCCESS, "账户中币的数量小于设定的交易数量,交易失败");
                        }
                    }
                }
                System.out.println("可以交易的" + sellCoin + "的数量为 ： " + sellForEthCount);

                //TODO 2.获取当前成交价格进行
                String currentPrice = aacoinService.getCurrentPrice(symbol); //卖出的虚拟币的价格
                System.out.println(symbol + " 当前交易价格  " + currentPrice);

                Double dCurrentPrice = Double.valueOf(currentPrice) - addRedPrice * reducePrice;
                System.out.println(symbol + " 卖出价格  " + dCurrentPrice.toString());

                sellForEthAmount = Double.valueOf(dCurrentPrice) * sellForEthCount;
                System.out.println(" 卖出获得的Eth数量  " + sellForEthAmount);

                if (sellForEthAmount < MINI_AMOUNT_ETH) { //如果总量小于最小的交易量取消
                    System.out.println("账户中的交易额度小于最小的交易额度");
//                    isCoinAvailable = false;
//                    break;
                    return new Response(Result.SUCCESS, "账户中的交易额度小于最小的交易额度,交易失败");
                } else {
                    isCoinAvailable = true;
                }

                System.out.println(" 卖出的价格  " + dCurrentPrice.toString() + "; 卖出的数量 ：" + sellForEthCount);
                if (isCoinAvailable) { //可以下单则进行下单操作
                    //TODO 下单
                    System.out.println("开始下单");
                    isMakeOrder = aacoinService.makeOrder(secretKey, accessKey, symbol, type, sellForEthCount.toString(), dCurrentPrice.toString());
                    reducePrice = reducePrice + 1;
                    if (!isMakeOrder) {
                        System.out.println("下单失败");
//                        break;//下单失败直接返回
                        return new Response(Result.SUCCESS, "下单失败");
                    }
                }

                //#################撤单 ，下单
                //TODO 5.order/currentOrders 获取当前委托订单
                //symbol 交易市场（例：BCC_ETH）
                currentOrders = aacoinService.currentOrders(secretKey, accessKey, symbol, type, "1", "10000");

                Gson gson = new Gson();
                OrderDetail orderDetail = gson.fromJson(currentOrders, OrderDetail.class);
                orderTotal = orderDetail.getData().getTotal();
                System.out.println("订单总数量 ： " + orderTotal);
                Integer sunmQuantity = 0;
                if (orderTotal > 0) {
                    List<CurrentOrders> ordersList = orderDetail.getData().getList();
                    for (CurrentOrders order : ordersList) {
                        //如果订单处于：尚未成交或者部分成交的状态，则取消订单
//TODO 订单状态没有返回Api-Bug  String status = order.getStatus();
//                        if (status.equals("open") || status.equals("partial_filled")) {
                        Integer quantityInt = Integer.valueOf(order.getQuantity());
                        sunmQuantity = sunmQuantity + quantityInt;
                        //当委托数量 大于0的情况下，即为没有完全成交，撤销订单
                        if (quantityInt > 0) {
                            //取消订单
                            String orderId = order.getOrderId();
                            ///order/cancel 撤单
                            Boolean cancelOrder = aacoinService.orderCancel(secretKey, accessKey, orderId);
                            if (cancelOrder) {
                                cancelOrderCount = cancelOrderCount + 1;
                                System.out.println("订单撤销成功 ：：： " + "订单号：：： " + orderId);
                            }
                        }
                        System.out.println("撤销订单总数量 ; " + cancelOrderCount);
                    }
                }

                if (!isCoinAvailable) {
                    break; //退出当前循环
                }
            }
            System.out.println("********************************************************************************");
        }

        returnResult = "试用版挖矿结束 :::: 感谢您的支持 ：：：更多请联系  微信syuukawa";
        response = new Response(Result.SUCCESS, returnResult);
        return response;
    }

    @ApiOperation(value = "accounts", notes = "查看账户信息 ")
    @Path("accounts")
    @POST
    @Produces({Constant.APPLICATION_JSON_UTF8})
    @Consumes({Constant.APPLICATION_JSON_UTF8})
    public Response<List<AccountsData>> getAccountDetails(@QueryParam("secretKey") String secretKey, @QueryParam("accessKey") String accessKey) throws IOException, InterruptedException {

        Response<List<AccountsData>> response = null;
        List<AccountsData> accountsDataList = aacoinService.getAccountCoins(secretKey, accessKey);
        System.out.println("账户信息 ：");
        for (AccountsData accountsData : accountsDataList) {
            System.out.println(accountsData.getCurrencyCode() + " :  " + accountsData.getAccounts().get(0).getBalance());
        }
        response = new Response(Result.SUCCESS, accountsDataList);
        return response;
    }
}
