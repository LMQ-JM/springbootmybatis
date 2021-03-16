package com.example.websocket.handle;


import com.alibaba.fastjson.JSONObject;
import com.example.websocket.util.MsgInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/3/11
 * @vesion 1.0
 **/
@Component
@Slf4j
@ChannelHandler.Sharable
public class TextWebSocketHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {


   @Override
   protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) {

      String text = textWebSocketFrame.text();

      //拿到channel对象
      Channel channel = ctx.channel();

      MsgInfo msgInfo = null;

      try {
         msgInfo = JSONObject.parseObject(text, MsgInfo.class);
         System.out.println(msgInfo.getType());
         if (msgInfo.getType() != 2) {
            log.info("收到一个非心跳消息");
         }

      } catch (Exception e) {
         log.info("消息类型转换错误");
         e.printStackTrace();
      }

      if (msgInfo != null && msgInfo.getType() > 0) {
         //符合协议，继续透传
         ctx.fireChannelRead(msgInfo);
      } else {
         //不符合协议
         log.info("传的数据有误...");
         channel.close();
      }


   }
}
