package com.daskrr.packetmanager;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.daskrr.packetmanager.PacketListener.ListenerType;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_14_R1.Packet;

public class DuplexHandler implements Listener
{
	public DuplexHandler ()
	{
		for (Player p : Bukkit.getOnlinePlayers())
			registerPlayer(p);
	}
	
	@EventHandler
    	public void onJoin (final PlayerJoinEvent e)
	{
		registerPlayer(e.getPlayer());
	}
	
	public void addAdapter (PacketAdapter adapter, ListenerType type)
	{
		if (type.equals(ListenerType.RECEIVE))
			PacketLibrary.PLUGIN.readHandlers.add(adapter);
		
		if (type.equals(ListenerType.SEND))
			PacketLibrary.PLUGIN.writeHandlers.add(adapter);
		
		for (Player p : Bukkit.getOnlinePlayers())
			refreshPlayer(p);
	}
	
	private boolean readCancellation = false;
	private boolean writeCancellation = false;
	
	private void refreshPlayer (Player p)
	{		
		final ChannelPipeline pipeline = ((CraftPlayer) p).getHandle().playerConnection.networkManager.channel.pipeline();
		if (pipeline.get("duplex_handler") == null)
		{
			registerPlayer(p);
			return;
		}

		pipeline.remove("duplex_handler");

		ChannelDuplexHandler cdh = createDuplexHandler(p);

		pipeline.addBefore("packet_handler", "duplex_handler", (ChannelHandler) cdh);
	}

	private void registerPlayer(Player p)
	{		
		final ChannelPipeline pipeline = ((CraftPlayer) p).getHandle().playerConnection.networkManager.channel.pipeline();
		if (pipeline.get("duplex_handler") != null)
			return;

		ChannelDuplexHandler cdh = createDuplexHandler(p);

		pipeline.addBefore("packet_handler", "duplex_handler", (ChannelHandler) cdh);
	}
	
	private ChannelDuplexHandler createDuplexHandler (Player p)
	{
		return new ChannelDuplexHandler()
		{
            public void channelRead(final ChannelHandlerContext context, Object packet) throws Exception
            {            	
            	for (PacketAdapter adapter : PacketLibrary.PLUGIN.readHandlers)
            	{
            		if (adapter.getListener() != null)
            		{
            			try
            			{
		            		adapter.getListener().onEvent(new PacketEvent ()
		        				{
		            				@Override
		            				public boolean compareType (Class<? extends Packet<?>> compare)
		            				{
		            					return compare.isInstance(packet);
		            				}
		            			
		            				@Override
		            				public Packet<?> getPacketInstance ()
		            				{
		            					return (Packet<?>) packet;
		            				}
		
									@Override
									public void setCancelled (boolean status)
									{
										readCancellation = status;
									}
		
									@Override
									public boolean getCancelled ()
									{
										return readCancellation;
									}
									
									@Override
									public Player getChannelOwner ()
									{
										return p;
									}
		        				});
            			}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
            		}
            	}
            	
            	if (!readCancellation)
            		super.channelRead(context, packet);
            }
            
            public void write(final ChannelHandlerContext context, Object packet, final ChannelPromise promise) throws Exception
            {
            	for (PacketAdapter adapter : PacketLibrary.PLUGIN.writeHandlers)
            	{
            		if (adapter.getListener() != null)
	            		adapter.getListener().onEvent(new PacketEvent ()
	        				{
		            			@Override
		        				public boolean compareType (Class<? extends Packet<?>> compare)
		        				{
		        					return compare.isInstance(packet);
		        				}
	            			
	            				@Override
	            				public Packet<?> getPacketInstance ()
	            				{
	            					return (Packet<?>) packet;
	            				}
	
								@Override
								public void setCancelled (boolean status)
								{
									writeCancellation = status;
								}
	
								@Override
								public boolean getCancelled ()
								{
									return writeCancellation;
								}
								
								@Override
								public Player getChannelOwner ()
								{
									return p;
								}
	        				});
            	}
            	
            	if (!writeCancellation)
            		super.write(context, packet, promise);
            }
        };
	}
}
