package com.example.weChatPay.util;

/**
 * @author MQ
 * @date 2021/3/18 16:17
 */
public class WeChatPayConfig {

    /**
     * 小程序appid  开发者自己拥有的
     */
    public static  final String appid="wx6f3fbf1454d85747";


    /**
     * 小程序秘钥
     */
    public static final String secret="3d39711670edf5003814761764f0c350";

    /**
     *商户ID
     */
    public static final String MCHID="1607486962";

    /**
     * 商户号秘钥
     */
    public static final String PATERNERKEY="UmZKqJqkhIO1KY9IBBrVJhqKDlVsGaFo";

    /**
     * 支付成功后的服务器回调url
     */
    public static final String notifyUrl = "http://192.168.0.226/OrdersController/WeChatNotify";

    /**
     * 交易类型
     */
    public static final String TRADETYPE = "JSAPI";

    /**
     * 微信统一下单接口地址
     */
    public static final String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 签名方式
     */
    public static final String SIGNTYPE = "MD5";



}
