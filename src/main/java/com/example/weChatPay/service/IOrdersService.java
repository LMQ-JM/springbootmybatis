package com.example.weChatPay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/18 16:50
 */
public interface IOrdersService {

    /**
     *
     * @param openId 微信code
     * @param request

     * @return
     */
    Map<String,Object> orders(String openId, HttpServletRequest request,  BigDecimal price, String body) throws Exception;

    /**
     * 支付成功回调
     * @param request
     * @param response
     * @throws Exception
     */
    void WeChatNotify(HttpServletRequest request, HttpServletResponse response)throws Exception;
}
