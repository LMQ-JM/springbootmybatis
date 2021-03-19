package com.example.websocket.handle;


import com.example.websocket.util.MsgInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
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



   @Override
   protected void channelRead0(ChannelHandlerContext ctx, MsgInfo msgInfo){
      if (msgInfo.getType() == 3) {
         //此消息是一个单聊消息
         log.info("收到一条单聊消息---" + msgInfo);
         ctx.channel().writeAndFlush(new TextWebSocketFrame(msgInfo.getMsg()));
      } else {
         //不是单聊消息，继续透传
         ctx.fireChannelRead(msgInfo);
      }
   }
}
