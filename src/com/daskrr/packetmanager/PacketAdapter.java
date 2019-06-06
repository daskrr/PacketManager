package com.daskrr.packetmanager;

import java.lang.reflect.Field;

import net.minecraft.server.v1_14_R1.Packet;

public class PacketAdapter
{
	private Packet<?> packet;
	
	public PacketAdapter (Packet<?> packet)
	{
		this.packet = packet;
	}
	
	public void setPacket (Packet<?> packet)
	{
		this.packet = packet;
	}
	
	public Packet<?> getPacket ()
	{
		return this.packet;
	}
	
	// modifying the packet
	public Object getField (String field)
	{
		return getFieldValue(packet.getClass(), packet, field);
	}
	
	public void setField (String field, Object value)
	{
		setField(packet.getClass(), packet, field, value);
	}
	
	
	// listener
	private PacketListener packetListener = null;
	public PacketAdapter (PacketListener listener)
	{
		packetListener = listener;
	}
	
	public PacketListener getListener ()
	{
		return packetListener;
	}
	
	
	// util
	private static Object getFieldValue (Class<?> clazz, Object instance, String field)
	{
		Field f;
		try {
			f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void setField(Class<?> clazz, Object instance, String field, Object value)
	{
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}
