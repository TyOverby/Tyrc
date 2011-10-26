package com.prealpha.tyrc.client;

import com.prealpha.tyrc.shared.Message;

public interface ClientMessageHandler {
	public void dealWithIt(Message m);
	public String getName();
}
