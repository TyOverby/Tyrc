package com.prealpha.tyrc.server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class ConnectionHandler extends Thread {
	private final Socket clientSocket;
	private final Server server;


	PrintStream out;
	BufferedReader in;
	public ConnectionHandler(Socket clientSocket,Server server){
		this.clientSocket=clientSocket;
		this.server=server;

		try{
			this.out = new PrintStream(this.clientSocket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader((this.clientSocket.getInputStream())));
		}
		catch(IOException ioe){
			this.kill();
			ioe.printStackTrace();
		}
	}

	private void listen(){
		System.err.println("listening");

		while(true){
			if(clientSocket.isClosed()){
				break;
			}
			try{
				System.err.println(in.readLine());
			}
			catch(java.net.SocketException se){
				break;
			}
			catch(IOException ioe){
				break;
			}
		}
	}

	public void writeTo(String message) throws IOException{
		out.println(message);
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
