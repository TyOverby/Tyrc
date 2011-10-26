package com.prealpha.tyrc.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.prealpha.tyrc.shared.Message;


public class Client extends Thread{
	Socket socket;

	private ClientMessageHandler messageHandler;

	DataOutputStream out;
	DataInputStream in;

	public void setMessageHandler(ClientMessageHandler m){
		this.messageHandler = m;
	}

	public void run(){
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
		ByteBuffer buff ;
		while(true){
			try {
				int size = in.readInt();
				buff = ByteBuffer.allocate(size);
				buff.putInt(size);
				for(int i=0;i<size-4;i++){
					byte cur = in.readByte();
					buff.put(cur);
				}

				messageHandler.dealWithIt(Message.decode(buff.array()));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void say(byte[] b) throws IOException{
		out.write(b);
	}
	public void say(Message m) throws IOException{
		say(m.toBytes());
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
}
