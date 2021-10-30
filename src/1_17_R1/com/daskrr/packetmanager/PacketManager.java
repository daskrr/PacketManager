package com.daskrr.packetmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.daskrr.packetmanager.PacketListener.ListenerType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;

public class PacketManager
{
	private DuplexHandler dh = new DuplexHandler();
	
	public PacketManager()
	{
		Bukkit.getPluginManager().registerEvents(dh, PacketPlugin.PLUGIN);
	}
	
	public void sendPacket (Player p, PacketAdapter adapter)
	{
		getNMSPlayer(p).b.a.sendPacket(adapter.getPacket());
	}
	
	public void sendGlobalPacket (PacketAdapter adapter)
	{		
		for (Player p : Bukkit.getOnlinePlayers())
			getNMSPlayer(p).b.a.sendPacket(adapter.getPacket());
	}

	public void registerEvents(PacketAdapter adapter, ListenerType type)
	{
		dh.addAdapter(adapter, type);
	}
	
	@SuppressWarnings({ "deprecation" })
	public static EntityPlayer getNMSPlayer (Player p) {
		return MinecraftServer.getServer().getPlayerList().getPlayer(p.getUniqueId());
	}
}
