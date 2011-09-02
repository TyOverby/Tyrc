package com.prealpha.tyrc.server;
import java.io.IOException;
import java.net.ServerSocket;


public class ListenerThread extends Thread{
	private final ServerSocket serverSocket;
	private final Server server;
	
	public ListenerThread(ServerSocket serverSocket, Server server){
		this.serverSocket = serverSocket;
		this.server=server;
	}
	
	public void run(){
		while(server.isRunning()){
			try {
				System.out.println("looking for connections");
				server.addClient(serverSocket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
