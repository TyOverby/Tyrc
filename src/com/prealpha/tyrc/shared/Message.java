package com.prealpha.tyrc.shared;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Message {
	public enum Type{
		JOIN, MESSAGE, DISCONNECT, UNKNOWN
	}
	
	private static final int SIZE_BYTE = 1;
	private static final int SIZE_INT = 4;
	
	public final Type type;
	public final String name;
	public final String message;
	
	public Message(Type type, String name, String message){
		this.type = type;
		this.name = name;
		this.message = message;
	}
	
	public byte[] toBytes(){
		Message m = this;
		
		int capacity = 0;
		
		byte[] nameBytes = stringToByte(m.name);
		byte[] messageBytes = stringToByte(m.message);
		
		capacity += SIZE_INT; // total length
		capacity += SIZE_BYTE; // type
		capacity += SIZE_INT; // name length
		capacity += nameBytes.length; //name
		capacity += SIZE_INT; // message length
		capacity += messageBytes.length; // message
		
		ByteBuffer buff = ByteBuffer.allocate(capacity);
		
		// total length
		buff.putInt(capacity);
		// type
		buff.put(getType(m.type));
		// name length
		buff.putInt(nameBytes.length);
		// name
		buff.put(nameBytes);
		//message length
		buff.putInt(messageBytes.length);
		//message
		buff.put(messageBytes);
		
		return buff.array();
	}

	public static Message decode(byte[] incomingMessage){
		Type type;
		String name;
		String msg;
		
		ByteBuffer buff = ByteBuffer.wrap(incomingMessage);
		
		buff.getInt();
		
		type = getType(buff.get());
		
		int nameLength = buff.getInt();
		name = getString(buff,nameLength);
		
		int msgLength = buff.getInt();
		msg = getString(buff, msgLength);
		
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
	
	private byte[] stringToByte(String s){
		return s.getBytes(Charset.forName("Unicode"));
	}
	
	private static String getString(ByteBuffer buff,int length){
		byte[] dstBytes = new byte[length];
		for(int i=0;i<length;i++){
			dstBytes[i]=buff.get();
			//System.out.println(dstBytes[i]);
		}
		return new String(dstBytes,Charset.forName("Unicode"));
	}
	
	public static void main(String... args){
		Message test = new Message(Type.MESSAGE,"Tyler Jensen","Sup guys, how is everything going.");
		byte[] bytes = test.toBytes();
		for(byte b:bytes){
			System.out.print(b+" ");
		}
		Message test2 = decode(bytes);
		
		System.out.println(test2.type);
		System.out.println(test2.name);
		System.out.println(test2.message);
 	}
}
