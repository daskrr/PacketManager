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
              if (e.compareType(PacketPlayOutSpawnEntityLiving.class))
              {
                    PacketAdapter adapter = new PacketAdapter(e.getPacketInstance());

                    e.getChannelOwner().sendMessage("You received details about the spawn of an entity with UUID:" + adapter.getField("b"));
              } 
	}
};

manager.registerEvents(new PacketAdapter (listener), ListenerType.SEND);
</pre>
<br/>
There are <code>ListenerType.RECEIVE</code> for when the server gets an packet from a player and <code>ListenerType.SEND</code> for when the server sends a packet to a client (Minecraft Player).
<br/>
When sending a packet:
<pre>
PacketAdapter adapter = new PacketAdapter(new PacketPlayOutSpawnEntityLiving(anEntity));
adapter.setField("b", UUID.randomUUID());
manager.sendPacket(player, adapter);
</pre>
or
<br/>
<pre>manager.sendGlobalPacket(adapter);</pre>
