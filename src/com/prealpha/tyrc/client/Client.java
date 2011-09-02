package com.prealpha.tyrc.client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	Socket socket;

	DataOutputStream out;
	BufferedReader in;

	public Client(){
		startup();
		listen();
		shutdown();
	}

	private void startup(){
		try {
			this.socket=new Socket("localhost",1337);
			this.out = new DataOutputStream(this.socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen(){
		while(true){
			String get;
			try {
				get = in.readLine();
				System.err.println(get);
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
