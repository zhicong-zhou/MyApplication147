package com.jzkl.util;

public interface Webcon {
    String url = "http://www.wanmotech.com/shop-api/api";
//    String url = "http://test.wanmotech.com/shop-api/api";
    String url2 = "http://www.wanmotech.com/shop-api/api";
    String img = "http://www.wanmotech.com/shop-admin";

    String share = "http://www.wanmotech.com/shop/index.html";
    /*=======================域名一=======================*/
    /*登录*/
    String login = "/user/login";
    /*验证码*/
    String telCode = "/user/smscode";
    /*退出*/
    String logout = "/user/logout";
    /*注册*/
    String register = "/user/register";
    /*密码修改*/
    String pwdUpdate = "/user/updatePassword";
    /*用户信息*/
    String userInfo = "/user/userInfo";
    /*先传到服务器*/
    String userServer = "/sys/oss/upload";
    /*上传头像*/
    String userImg = "//user/updateHeadImg";
    /*消息*/
    String HomeInfo = "/user/getMessageLit";
    /*获取 商户认证的信息*/
    String my_shop_renz_info = "/user/getIdentificationInfo";
    /*商户认证*/
    String my_shop_renz = "/user/merchantIdentification";
    /*用户认证*/
    String my_user_renz = "/user/realNameIdentification";
    /*信用卡申请*/
    String home_xyk_register = "/user/applyCreditCard";
    /*信用卡列表*/
    String home_xyk_list = "/user/getCreditCardTransactList";
    /*我的钱包： 余额列表*/
    String wallet_yue_list = "/user/getBalanceRecordList";
    /*我的钱包： 积分明细*/
    String wallet_jifen_list = "/user/getCreditRecordList";
    /*提现*/
    String wallet_cash = "/user/withdraw";




    /*优惠券列表*/
    String My_reduction_list = "/user/getDiscountList";
    /*电子卷详情id*/
    String My_reduction_detail = "/user/getDiscountDetail";
    /*电子卷下单*/
    String My_reduction_pay = "/user/addDiscountOrder";
    /*电子券订单列表*/
    String My_dianzi_list = "/user/getDiscountOrderList";
    /*电子卷订单详情*/
    String My_dianzi_order_detail = "/user/getDiscountOrderDetail";
    /*首页推荐电子卷 list*/
    String home_dianzi_list = "/user/getDiscountTopList";

    /*================推广===================*/
    /*推广用户信息*/
    String incomeUserInfo = "/promotion/promotinInfo";
    /*推广更多 下级信息*/
    String incomeMore = "/promotion/moreLower";
    /*二维码*/
    String er_coed = "/user/getQrCodeApp?";
    /*消息模板*/
    String incomeMore_info_mb = "/promotion/getMessageTemplateList";

    /*===================首页===========================*/
    /*轮播图*/
    String home_banner = "/user/getIcon";
    /*反馈信息*/
    String problem_info = "/user/getFeedbackTypeInfo";
    /*提交反馈*/
    String problem_sub = "/user/fault";
    /*反馈列表*/
    String problem_list = "/user/getFaultList";
    /*关怀  过生日的人*/
    String birthUserList = "/user/getBirthUserList";
    /*送上祝福*/
    String birthday_blessing = "/promotion/sendMessage";
    /*送上祝福列表*/
    String birthday_blessing_list = "/promotion/moreLower";
    /*条形码 确认优惠卷*/
    String home_txm_confirm = "/user/confirmDiscountOrder";
    /*贷款*/
    String home_credit_apply = "/user/applyCreditLoans";


    /*添加地址*/
    String address_add = "/user/insertAddress";
    /*地址列表*/
    String address_list = "/user/getAddressList";
    /*删除地址*/
    String address_del = "/user/deleteAddress";
    /*修改地址*/
    String address_eid = "/user/updateAddress";
    /*========================商城============================*/
    /*商城分类*/
    String shop_category = "/user/getClassfyList";
    /*商城分类 详细列表*/
    String shop_category_list = "/user/getItemList";
    /*商品详情*/
    String shop_detail = "/user/getItem";
    /*商品规格*/
    String shop_size = "/user/getStandardList";
    /*加入购物车*/
    String shop_add_cart = "/user/addCart";
    /*购物车列表*/
    String shop_cart_list = "/user/getCartList";
    /*立即购买 订单生产*/
    String shop_order_buy = "/user/addOrder";
    /*购物车 订单生产*/
    String shop_order_carbuy = "/user/cartAddOrder";
    /*订单列表*/
    String shop_order_list = "/user/getOrderList";
    /*获取订单 支付的参数*/
    String shop_order_payData = "/user/getSandPayData";
    /*删除购物车*/
    String shop_car_deleta = "/user/deleteCart";
    /*购物车 加减*/
    String shop_car_updata = "/user/updateCart";
    /*取消订单*/
    String shop_order_deleta = "/user/deleteOrder";
    /*确认收货*/
    String shop_order_confrim = "/user/confirmOrder";
    /*订单成功与否 的code*/
    String shop_order_code = "/user/querySandPayStatus";
    /*升级*/
    String shop_add_SubOrder = "/user/addSubOrder";

    /*积分商品 列表*/
    String shop_Credit_list = "/user/getCreditItemList";
    /*积分下单*/
    String shop_Credit_pay = "/user/creditPay";
    /*分类 一级*/
    String shop_category_first = "/user/getTopClassList";
    /*二级*/
    String shop_category_second = "/user/getClassfyList1";
    /*搜索*/
    String shop_category_search = "/user/search";



    /*=======================域名一==结束=====================*/
    /*绑定银行卡 收入的*/
    String bank_binding = "/auth/auth";
    /*查询银行卡 信息*/
    String bank_binding_info = "/auth/authInfo";
    /*修改银行卡 信息*/
    String bank_binding_upd = "/auth/updateAuthInfo";

    /*银行卡 绑定 消费的*/
    String bank_bindingXf = "/bankcard/bind";
    /*验证码*/
    String bank_binding_yancode = "/bankcard/smscode";
    /*确认绑定*/
    String bank_binding_Confirm = "/bankcard/bindConfirm";
    /*银行卡列表*/
    String bank_binding_list = "/bankcard/getBankcard";
    /*解绑*/
    String bank_binding_unbind = "/bankcard/unbind";
    /*确认刷卡*/
    String bank_binding_pay = "/pay/pay";
    /*刷卡验证码*/
    String bank_binding_paycode = "/pay/smscode";
    /*刷卡确认*/
    String bank_binding_paypayConfirm = "/pay/payConfirm";
    /*我的交易*/
    String bank_binding_Record = "/pay/getPayRecord";
}
