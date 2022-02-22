package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.cloudtools.CaseType;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
/**
 * @Author: test
 * */
public class SocketHandler {
	/**记录访问的client*/
	private static ConcurrentMap<String, AioSession> socketMap;
	/**记录session对应的ip*/
	private static ConcurrentMap<String, String> sessionIdMap;
	/**client锁定表:0,未锁定;1,锁定*/
	private static ConcurrentMap<String, Map<CaseType, Integer>> socketlock;
	
	private static ConcurrentMap<String, List<String>> klocNameMap;
	
	/**记录port*/
	private static ConcurrentMap<String, String> portMap;
	
	public static void init() {
		
		log.info("初始化socket");
		socketMap = new ConcurrentHashMap(2000);
		socketlock = new ConcurrentHashMap(2000);
		sessionIdMap = new ConcurrentHashMap(2000);
		klocNameMap = new ConcurrentHashMap(2000);
		portMap = new ConcurrentHashMap(2000);
	}
	
	
	public static ConcurrentMap<String, String> getPortMap() {
		return portMap;
	}


	public static ConcurrentMap<String, AioSession> getSocketMap() {
		
		return socketMap;
	}

	public static ConcurrentMap<String, List<String>> getKlocNameMap() {
		return klocNameMap;
	}


	public static ConcurrentMap<String, Map<CaseType, Integer>> getSocketLock() {
		
		return socketlock;
		
	}

	public static ConcurrentMap<String, String> getSessionIdMap() {
		return sessionIdMap;
	}

	public static void addSocketMap(String key,AioSession session) {

		socketMap.put(key, session);
		socketlock.put(key, new HashMap<>(2000));
		sessionIdMap.put(session.getSessionID(), key);
	}
	
	public static void removeSocketMap(String key) {
		if(socketMap.containsKey(key)) {
			socketMap.remove(key);
			socketlock.remove(key);
			portMap.remove(key);
		}
		if(klocNameMap.containsKey(key)) {
			klocNameMap.remove(key);
		}
	}
	
	public static boolean write(String key,String msg) {
		
		if(!socketMap.containsKey(key)) {
			return false;
		}
		
		WriteBuffer outputStream = socketMap.get(key).writeBuffer();
        try {
            byte[] bytes = msg.getBytes();
            outputStream.writeInt(bytes.length);
            outputStream.write(bytes);
            outputStream.flush();
            return true;
        } catch (IOException e) {
        	return false;
        }
	}
	
	
}
