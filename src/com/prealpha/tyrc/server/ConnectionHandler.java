package com.prealpha.tyrc.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.prealpha.tyrc.shared.Message;


public class ConnectionHandler extends Thread {
	private final Socket clientSocket;
	private final Server server;


	DataOutputStream out;
	DataInputStream in;
	public ConnectionHandler(Socket clientSocket,Server server){
		this.clientSocket=clientSocket;
		this.server=server;

		try{
			this.out = new DataOutputStream(this.clientSocket.getOutputStream());
			this.in = new DataInputStream(this.clientSocket.getInputStream());
		}
		catch(IOException ioe){
			this.kill();
			ioe.printStackTrace();
		}
	}

	private void listen(){
		System.err.println("listening");
		byte[] b = new byte[100000];

		while(true){
			if(clientSocket.isClosed()){
				break;
			}
			try{
				// TODO: read data from in
			}
			catch(java.net.SocketException se){
				break;
			}
			catch(IOException ioe){
				break;
			}
		}
	}

	public void writeTo(Message m) throws IOException{
		out.write(m.toBytes());
	}

	private void kill(){
		System.err.println("killed");
		server.removeClient(this);
	}

	@Override
	public void run(){
		listen();		
		this.kill();
	}
}
