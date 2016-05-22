package io.github.Jacksparks2.SleepPercent;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SleepPercent extends JavaPlugin implements org.bukkit.event.Listener{
	public List<Player> sleepPlayers = new java.util.ArrayList<Player>();
	private int sleepPercent;
	
	public void onEnable()
	{
		loadConfig();
		getServer().getPluginManager().registerEvents(this,  this);
		getLogger().info("SleepPercent Loaded!");
		super.onEnable();
	}
	
	public void onDisable()
	{
		super.onDisable();
	}
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event)
	{
		this.sleepPlayers.add(event.getPlayer());
		if (sleepPlayers.size() != getServer().getOnlinePlayers().length)
			testPercent();
	}
	
	public void onBedLeave(PlayerBedLeaveEvent event)
	{
		if (sleepPlayers.size() != getServer().getOnlinePlayers().length)
		{
			this.sleepPlayers.remove(event.getPlayer());
			if (org.bukkit.Bukkit.getWorld("world").getTime() >= 12000L)
				getServer().broadcastMessage(event.getPlayer().getDisplayName() + "§6 is no longer sleeping");
		}
	}
	
	private void testPercent()
	{
		int totalPlayers = getServer().getOnlinePlayers().length;
		float percent = new Float(this.sleepPlayers.size()).floatValue() / new Float(totalPlayers).floatValue() * new Float(100.0F).floatValue();
		getServer().broadcastMessage(((Player)this.sleepPlayers.get(this.sleepPlayers.size() - 1)).getDisplayName() + "§6 is now sleeping. " + this.sleepPlayers.size() + "/" + totalPlayers + " (" + new Float(this.sleepPlayers.size()).floatValue() / new Float(totalPlayers).floatValue() * new Float(100.0F).floatValue() + "%)");
		
		if (percent >= new Float(this.sleepPercent).floatValue())
		{
			World world = org.bukkit.Bukkit.getWorld("world");
			world.setTime(1000L);
			if (world.hasStorm())
			{
				world.setWeatherDuration(0);
				world.setThunderDuration(0);
				world.setThundering(false);
			}
			this.sleepPlayers.clear();
			getServer().broadcastMessage("§6Wakey, wakey, rise and shine... Good Morning everyone!");
		}
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		this.sleepPercent = getConfig().getInt("percent");
	}
}
