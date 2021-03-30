package com.example.websocket.service.impl;


import com.example.websocket.service.PushService;
import com.example.websocket.util.GroupUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MQ
 * @date 2021/3/13 16:26
 */
@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private GroupUtils groupUtils;


    @Override
    public void pushMsgToOne(String userId, String msg) {
        Channel channel = groupUtils.getChannel(userId);
        //对方不在线
        if(channel==null){
            System.out.println("对方不在线");
        }

        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void pushMsgToAll(String msg) {
      /*  groupUtils.getMap().writeAndFlush(new TextWebSocketFrame("群发"));*/
    }
}
