package com.prealpha.tyrc.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	public static final int SERVER_PORT = 1337;
	private boolean isRunning;

	private ListenerThread listenerThread;

	private ServerSocket serverSocket;
	private List<ConnectionHandler> clientSocketReaders = new ArrayList<ConnectionHandler>();

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
		clientSocketReaders.add(ncon);
		ncon.start();
		try {
			ncon.writeTo("welcome\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("added client");
	}
	protected void removeClient(ConnectionHandler socketReader){
		this.clientSocketReaders.remove(socketReader);
	}
	public boolean isRunning(){
		return this.isRunning;
	}

	public void run(){
		startup();
		listen();
		shutdown();
	}

	public static void main(String...args){
		Server server = new Server();
		server.run();
	}
}
