package com.daskrr.packetmanager;

import org.bukkit.entity.Player;

import net.minecraft.network.protocol.Packet;

public interface PacketEvent
{
	public boolean compareType (Class<? extends Packet<?>> clazz);
	public Packet<?> getPacketInstance();
	public void setCancelled (boolean status);
	public boolean getCancelled ();
	public Player getChannelOwner();
}
