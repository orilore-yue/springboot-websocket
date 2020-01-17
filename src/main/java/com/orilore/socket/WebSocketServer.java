package com.orilore.socket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wwserver")
public class WebSocketServer {
	//声明线程安全Set，用来存放每个客户端对应的Session对象
	private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<Session>();
	
	/**
	 * 连接建立成功调用的方法 
	 * @param session
	 */
	@OnOpen
	public void open(Session session) {
		sessions.add(session);
		send(session,"Socket连接成功，会话ID为："+session.getId());
	}
	
	/**
	 * 连接关闭调用的方法 
	 * @param session
	 */
	@OnClose
	public void close(Session session)  {
		sessions.remove(session);
	}
	
	/**
	 * 接收到客户端消息时调用的方法
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void receive(String message,Session session){
		send(session,"服务器收到的消息是："+message);
	}
	
	/**
	 * 发送错误时调用的方法 
	 * @param session
	 * @param e
	 */
	@OnError
	public void error(Session session,Throwable e) {
		System.out.println("会话"+session.getId()+"发送错误，"+e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * 向客户端发送消息的方法
	 * @param session
	 * @param message
	 */
	public static void send(Session session,String message){
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向特定客户端发送消息的方法
	 * @param id
	 * @param message
	 */
	public static void sendOne(String id,String message){
		try {
			for(Session session : sessions) {
				if(session.getId().equals(id)) {
					session.getBasicRemote().sendText(message);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 广播消息的方法
	 * @param message
	 */
	public static void broadCast(String message){
		try {
			for(Session session : sessions) {
				session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
