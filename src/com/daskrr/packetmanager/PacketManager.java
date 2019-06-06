package com.daskrr.packetmanager;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.daskrr.packetmanager.PacketListener.ListenerType;

public class PacketManager
{
	private DuplexHandler dh = new DuplexHandler();
	
	public PacketManager()
	{
		Bukkit.getPluginManager().registerEvents(dh, PacketLibrary.PLUGIN);
	}
	
	public void sendPacket (Player p, PacketAdapter adapter)
	{
		((CraftPlayer) p).getHandle().playerConnection.networkManager.sendPacket(adapter.getPacket());
	}
	
	public void sendGlobalPacket (PacketAdapter adapter)
	{
		for (Player p : Bukkit.getOnlinePlayers())
			((CraftPlayer) p).getHandle().playerConnection.networkManager.sendPacket(adapter.getPacket());
	}

	public void registerEvents(PacketAdapter adapter, ListenerType type)
	{
		dh.addAdapter(adapter, type);
	}
}
