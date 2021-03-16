package com.example.websocket.handle;


import com.example.websocket.util.GroupUtils;
import com.example.websocket.util.MsgInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/4/2
 * @vesion 1.0
 **/
@Component
@Slf4j
@ChannelHandler.Sharable
public class ConnectWebSocketHandle extends SimpleChannelInboundHandler<MsgInfo> {

   @Autowired
   private GroupUtils groupUtils;

   /**
    * 一个连接上线
    * @param ctx
    * @throws Exception
    */
   @Override
   public void channelRegistered(ChannelHandlerContext ctx) {
      log.info("有一个新的连接上线");
   }


   /**
    * 一个连接下线
    * @param ctx
    * @throws Exception
    */
   @Override
   public void channelUnregistered(ChannelHandlerContext ctx) {
      log.info("有一个连接下线");
      groupUtils.removeByChannel(ctx.channel());
   }


   @Override
   protected void channelRead0(ChannelHandlerContext ctx, MsgInfo msgInfo) {
      System.out.println(msgInfo.getType());
      if (msgInfo.getType() == 1) {
         //连接请求
         Channel channel = ctx.channel();

         //设备id
         String cid = msgInfo.getCid();

         //将设备Id与连接通道进行绑定
         groupUtils.put(cid,channel);

         log.info("握手成功--当前连接数：" + groupUtils.size());
         ctx.channel().writeAndFlush(new TextWebSocketFrame("握手成功"));
      } else {
         //非握手请求，继续往后透传
         ctx.fireChannelRead(msgInfo);
      }

   }


   /**
    * 异常处理
    * @param ctx
    * @param cause
    * @throws Exception
    */
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      log.info("连接发生异常,异常是:" +cause);
      groupUtils.removeByChannel(ctx.channel());
   }


}
