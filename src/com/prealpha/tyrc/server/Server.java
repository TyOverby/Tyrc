package com.prealpha.tyrc.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import com.prealpha.tyrc.shared.Message;
import com.prealpha.tyrc.shared.Message.Type;

public class Server extends Thread {
	public final int SERVER_PORT;
	private boolean isRunning;

	private ListenerThread listenerThread;
	private final ServerMessageHandler smh;

	private ServerSocket serverSocket;
	private List<ConnectionHandler> connectionHandlers = new ArrayList<ConnectionHandler>();

	public Server(int port,ServerMessageHandler smh){
		this.SERVER_PORT=port;
		this.smh = smh;
		this.start();
	}
	
	private void startup(){
		System.out.println("startup()");

		try {
			this.serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.err.println("could not listen on port "+SERVER_PORT);
		}
		this.isRunning=true;

		this.listenerThread = new ListenerThread(serverSocket, this);
		this.listenerThread.start();
	}
	private void listen(){
		System.out.println("listen()");

		while(this.isRunning()){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void shutdown(){
		System.out.println("shutdown()");

		try {
			this.serverSocket.close();
		} catch (IOException e) {
			System.err.println("could not close socket");
		}

		this.isRunning=false;
	}

	protected void addClient(Socket clientSocket){
		ConnectionHandler ncon = new ConnectionHandler(clientSocket,this);
		connectionHandlers.add(ncon);
		ncon.start();
		try {
			ncon.say(new Message(Message.Type.WELCOME,"[Server]", "Welcome"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("added client");
	}
	protected void removeClient(ConnectionHandler socketReader){
		this.connectionHandlers.remove(socketReader);
		if(socketReader.name!=null){
			this.say(new Message(Type.DISCONNECT,socketReader.name,"has disconnected"));
		}else{
			this.say(new Message(Type.DISCONNECT,"a user","has disconnected"));
		}
	}
	public boolean isRunning(){
		return this.isRunning;
	}

	public void say(Message m){
		this.say(m.toBytes());
		smh.dealWithIt(m);
	}

	public void say(byte[] b){
		for(ConnectionHandler c:this.connectionHandlers){
			try {
				c.say(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		smh.dealWithIt(Message.decode(b));
	}

	public void run(){
		startup();
		listen();
		shutdown();
	}
}
