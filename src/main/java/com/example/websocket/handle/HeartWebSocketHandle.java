package com.example.websocket.handle;


import com.example.websocket.util.GroupUtils;
import com.example.websocket.util.MsgInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class HeartWebSocketHandle extends SimpleChannelInboundHandler<MsgInfo> {


   @Autowired
   private GroupUtils groupUtils;

   @Override
   protected void channelRead0(ChannelHandlerContext ctx, MsgInfo msgInfo) {
      if (msgInfo.getType() == 2) {
         //当前是一个心跳信息
         Channel channel = groupUtils.getChannel(msgInfo.getCid());
         if (channel == null) {
            log.info("请先连接握手~");
         }

         if (channel != null) {
            log.info("收到心跳消息");
            //接收到客户端给服务器的心跳消息 ping帧
            //服务器给客户端回一个心跳 pong帧
         }
      } else {
         //非心跳消息
         ctx.fireChannelRead(msgInfo);
      }
   }
}
