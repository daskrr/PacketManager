# PacketManager
Small Packet Handling Library (for Spigot 1.14.1)

To get the instance of PacketManager (the handler of sending/listening for packets):
<br/>
<code>PacketManager manager = PacketPlugin.getPacketManager();</code>
<br/>
or
<br/>
<code>PacketManager manager((PacketLibrary) Bukkit.getPluginManager().getPlugin("PacketManager")).getPacketManager();</code>

Example of Listening usage:<br/>
<pre>PacketListener listener = new PacketListener()
{
	@Override
	public void onEvent(PacketEvent e)
	{	
              e.getChannelOwner().sendMessage("You sent a " + e.getPacketInstance().getClass().getName() + ".");
	}
};

manager.registerEvents(new PacketAdapter (listener), ListenerType.RECEIVE);
</pre>
<br/>
There are <code>ListenerType.RECEIVE</code> for when the server gets an packet from a player and <code>ListenerType.SEND</code> for when the server sends a packet to a client (Minecraft Player).
<br/>
When sending a packet:
<pre>
PacketAdapter adapter = new PacketAdapter(new PacketPlayOutSpawnEntityLiving(...));

manager.sendPacket(player, adapter);
</pre>
or
<code>manager.sendGlobalPacket(adapter);</code>
