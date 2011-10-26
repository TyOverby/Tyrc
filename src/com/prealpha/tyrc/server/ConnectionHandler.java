package com.prealpha.tyrc.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.prealpha.tyrc.shared.Message;


public class ConnectionHandler extends Thread {
	private final Socket clientSocket;
	private final Server server;

	public String name;


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
		ByteBuffer buff;

		while(true){
			if(clientSocket.isClosed()){
				break;
			}
			try{
				int size = in.readInt();
				buff = ByteBuffer.allocate(size);
				buff.putInt(size);
				for(int i=0;i<size-4;i++){
					byte cur = in.readByte();
					buff.put(cur);
				}
				this.server.say(buff.array());

				if(this.name==null){
					Message m = Message.decode(buff.array());
					this.name=m.name;
				}
			}
			catch(java.net.SocketException se){
				break;
			}
			catch(IOException ioe){
				break;
			}
		}
	}

	public void say(byte[] b) throws IOException{
		out.write(b);
	}
	public void say(Message m) throws IOException{
		say(m.toBytes());
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
