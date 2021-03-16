package com.example.websocket.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 此处只接收文件
 * @author qf
 * @date 2020/4/3
 * @vesion 1.0
 **/
@Component
@Slf4j
@ChannelHandler.Sharable
public class FileWebSocketHandle extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {


   @Override
   protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame binaryWebSocketFrame) {
      log.info("服务器接收到二进制消息,消息长度:[{}]", binaryWebSocketFrame.content().capacity());


      ByteBuf content = binaryWebSocketFrame.content();
      content.markReaderIndex();
      int flag = content.readInt();
      log.info("标志位:[{}]", flag);
      content.resetReaderIndex();
      ByteBuf byteBuf = Unpooled.directBuffer(binaryWebSocketFrame.content().capacity());
      byteBuf.writeBytes(binaryWebSocketFrame.content());

      ctx.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
   }
}
