package com.example.websocket.service.impl;


import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.message.dao.MessageMapper;
import com.example.message.entity.ChatRecord;
import com.example.websocket.service.PushService;
import com.example.websocket.util.GroupUtils;
import com.example.websocket.util.MsgInfo;
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

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void pushMsgToOne(String userId, String msg) {
        Channel channel = groupUtils.getChannel(userId);
        //对方不在线
        if(channel==null){
            System.out.println("对方不在线");
        }
        ChatRecord chatRecord=new ChatRecord();
        chatRecord.setFUserId(267);
        chatRecord.setJUserId(260);
        chatRecord.setCreateAt(System.currentTimeMillis()/1000+"");
        chatRecord.setMCode(Long.valueOf("822772057426366464"));
        chatRecord.setMessage(msg);
        chatRecord.setMessageType(0);

        int i = messageMapper.addMessage(chatRecord);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"消息推送失败");
        }

        System.out.println(userId);
        MsgInfo msgInfo =new MsgInfo();
        msgInfo.setType(3);
        msgInfo.setMsg(msg);
        msgInfo.setToId(userId);
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void pushMsgToAll(String msg) {
      /*  groupUtils.getMap().writeAndFlush(new TextWebSocketFrame("群发"));*/
    }
}
