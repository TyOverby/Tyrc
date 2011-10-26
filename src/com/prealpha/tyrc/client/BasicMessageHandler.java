package com.prealpha.tyrc.client;

import java.io.IOException;

import com.prealpha.tyrc.shared.Message;
import com.prealpha.tyrc.shared.Message.Type;

public class BasicMessageHandler implements ClientMessageHandler {

	public static final String name = "Ty";
	private Client client;

	public BasicMessageHandler(Client c){
		this.client=c;
		this.client.setMessageHandler(this);
		this.client.start();
	}

	@Override
	public void dealWithIt(Message m) {
		System.out.println(m.type);
		System.out.println(m.name);
		System.out.println(m.message);
		if(m.type==Type.WELCOME){
			try {
				client.say(new Message(Type.JOIN,this.getName(),this.getName()+" has joined the serve"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String ... args){
		new BasicMessageHandler(new Client());
	}

	@Override
	public String getName() {
		return "Ty";
	}
}
