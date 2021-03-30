package com.example.websocket.handle;


import com.alibaba.fastjson.JSON;
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
public class OneChatWebSocketHandle extends SimpleChannelInboundHandler<MsgInfo> {


   @Autowired
   private GroupUtils groupUtils;



   @Override
   protected void channelRead0(ChannelHandlerContext ctx, MsgInfo msgInfo){
      if (msgInfo.getType() == 3) {
         //此消息是一个单聊消息
         log.info("收到一条单聊消息---" + msgInfo);

         Channel channel = groupUtils.getChannel(msgInfo.getToId());


         if(channel==null){
            log.info("对方不在线");
         }

         if(channel!=null){
            log.info("发送消息");
            //3代表 单聊和消息列表
           // msgInfo.setType(3);
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgInfo)));
         }

      } else {
         //不是单聊消息，继续透传
         ctx.fireChannelRead(msgInfo);
      }
   }
}
