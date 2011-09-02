package com.prealpha.tyrc.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.prealpha.tyrc.shared.Message;


public class Client {
	Socket socket;

	DataOutputStream out;
	DataInputStream in;

	public Client(){
		startup();
		listen();
		shutdown();
	}

	private void startup(){
		try {
			this.socket=new Socket("localhost",1337);
			this.out = new DataOutputStream(this.socket.getOutputStream());
			this.in = new DataInputStream(this.socket.getInputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen(){
		byte[] b = new byte[100000];
		while(true){
			try {
				in.readFully(b);
				
				System.err.println(Message.decode(b).message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void shutdown(){
		if(this.socket!=null){
			try {
				this.in.close();
				this.out.close();
				this.socket.close();
			} catch (IOException e) {
				System.err.println("failed to close client socket");
			}
		}
	}


	public static void main(String...args){
		new Client();
	}
}
