package org.uts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description WebSocket服务类
 * @Author codBoy
 * @Date 2024/8/24 22:03
 */
@ServerEndpoint("/{userId}")
@Component
@Slf4j
public class WebsocketServer {
    public static Map<Long, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId){
        log.info("[WebSocket] 有新的客户端连接上来了：{}", userId);
        SESSION_MAP.put(userId, session);
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId){
        log.info("[WebSocket] 连接准备关闭：{}", userId);
        SESSION_MAP.remove(userId);
    }

    @OnError
    public void onError(Throwable throwable){
        log.warn("[WebSocket] 连接出现异常", throwable);
    }
}
