package com.example.weChatPay.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.weChatPay.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/18 16:40
 */
@Api(tags = "支付API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/OrdersController")
public class OrdersController {

    @Autowired
    private IOrdersService iOrdersService;

    /**
     *
     * 微信浏览器内微信支付/公众号支付(JSAPI)
     * @return
     */
    @ApiOperation(value = "微信浏览器内微信支付/公众号支付(JSAPI)",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/orders")
    public Map<String,Object> orders(String openid,HttpServletRequest request,BigDecimal price,String body) throws Exception {
        if("undefined".equals(openid)){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iOrdersService.orders(openid,request,price,body);
    }


    @ApiOperation(value = "微信支付回调",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/WeChatNotify")
    public void WeChatNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
