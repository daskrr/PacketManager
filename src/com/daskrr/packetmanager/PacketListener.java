package com.daskrr.packetmanager;

public interface PacketListener
{
	public void onEvent (PacketEvent e);
	
	public static enum ListenerType
	{
		RECEIVE,
		SEND;
	}
}

