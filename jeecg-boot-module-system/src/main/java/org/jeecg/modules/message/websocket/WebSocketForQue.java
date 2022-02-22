package org.jeecg.modules.message.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@ServerEndpoint("/websocket/getQue") //此注解相当于设置访问URL
/**
 * @Author chop
 * @Date 2021/06/07
 * @Description: 用来实时更新任务队列
 * */
public class WebSocketForQue {
    
    private Session session;
    
    private static CopyOnWriteArraySet<WebSocketForQue> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();

    @Override
    public int hashCode(){
        return super.hashCode();
    };

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
			this.session = session;
			webSockets.add(this);
			sessionPool.put(userId, session);
			log.info("【websocket-QUE】有新的连接，总数为:"+webSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnClose
    public void onClose() {
        try {
			webSockets.remove(this);
			log.info("【websocket-QUE】连接断开，总数为:"+webSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnMessage
    public void onMessage(String message) {
    	String getTime="getTime";
    	if(getTime.equals(message)) {
    		
    		session.getAsyncRemote().sendText(String.valueOf(System.currentTimeMillis()));
    	}
    	
    }
    
    
    /**此为广播消息*/
    public void sendAllMessage(String message) {
    	log.info("【websocket-QUE】广播消息:"+message);
        for(WebSocketForQue webSocket : webSockets) {
            try {
            	if(webSocket.session.isOpen()) {
            		webSocket.session.getAsyncRemote().sendText(message);
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**此为单点消息*/
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
            	log.info("【websocket-QUE】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**此为单点消息(多人)*/
    public void sendMoreMessage(String[] userIds, String message) {
    	for(String userId:userIds) {
    		Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                	log.info("【websocket-QUE】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    	}
        
    }
    
}