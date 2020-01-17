package com.orilore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orilore.socket.WebSocketServer;

@RestController
public class SocketCtrl {
	@RequestMapping("/server/broad")
	public void broad(String msg) {
		WebSocketServer.broadCast(msg);
	}
	
	@RequestMapping("/server/send")
	public void send(String id, String msg) {
		WebSocketServer.sendOne(id,msg);
	}
}
