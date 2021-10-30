package com.daskrr.packetmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class PacketPlugin extends JavaPlugin
{
	public static PacketPlugin PLUGIN;
	public List<PacketAdapter> writeHandlers = new ArrayList<PacketAdapter>();
	public List<PacketAdapter> readHandlers = new ArrayList<PacketAdapter>();
	
	private static PacketManager packetManager;
	
	@Override
	public void onEnable ()
	{
		PLUGIN = this;
		
		packetManager = new PacketManager();
	}
	
	public static PacketManager getPacketManager ()
	{
		return packetManager;
	}
}
