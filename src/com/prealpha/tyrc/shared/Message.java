package com.prealpha.tyrc.shared;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Message {
	public enum Type{
		JOIN, MESSAGE, DISCONNECT, UNKNOWN
	}
	
	public final Type type;
	public final String name;
	public final String message;
	
	private Message(Type type, String name, String message){
		this.type = type;
		this.name = name;
		this.message = message;
	}
	
	public static byte[] bytesFromMessage(Message m){
		ByteBuffer buff = ByteBuffer.allocate(1+1+m.name.length()+1+m.message.length());
		buff.put(getType(m.type));
		buff.put((byte)m.name.length());
		buff.put(m.name.getBytes(Charset.forName("ASCII")));
		buff.put((byte)m.message.length());
		buff.put(m.message.getBytes(Charset.forName("ASCII")));
		
		return buff.array();
	}

	public static Message decode(byte[] incomingMessage){
		String name = "";
		String msg = "";
		
		int curpos = 0;
		Type type = getType(incomingMessage[curpos]);
		curpos += 1;
		
		name = getString(incomingMessage,(byte)curpos);
		curpos+=name.length()+1;
		
		msg = getString(incomingMessage,(byte)curpos);
		
		return new Message(type,name,msg);
	}

	private static byte getType(Type t){
		switch(t){
		case JOIN:
			return 0x00;
		case MESSAGE:
			return 0x01;
		case DISCONNECT:
			return 0x02;
		default:
			return 0x03;
		}
	}
	
	private static Type getType(byte b){
		switch(b){
		case 0x00:
			return Type.JOIN;
		case 0x01:
			return Type.MESSAGE;
		case 0x02:
			return Type.DISCONNECT;
		default:
			return Type.UNKNOWN;
		}
	}
	
	private static String getString(byte[] totalBytes,byte startingpos){
		short stringLength = totalBytes[startingpos];
		
		byte[] stringBytes = new byte[stringLength];
		for(int o=startingpos+1,n=0;n<stringLength;n++,o++){
			stringBytes[n]=totalBytes[o];
		}
		
		Charset charset = Charset.forName("ASCII");
		return new String(stringBytes,charset);
	}
	
	public static void main(String... args){
		Message test = new Message(Type.MESSAGE,"Ty","hey guys");
		byte[] bytes = (bytesFromMessage(test));
		Message test2 = decode(bytes);
		
		System.out.println(test2.type);
		System.out.println(test2.name);
		System.out.println(test2.message);
 	}
}
