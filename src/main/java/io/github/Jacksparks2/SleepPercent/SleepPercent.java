package io.github.Jacksparks2.SleepPercent;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SleepPercent extends JavaPlugin
{
	public List<Player> sleepDelayPlayers = new java.util.ArrayList<Player>();
	public List<Player> sleepPlayers = new java.util.ArrayList<Player>();
	public int miningPlayers;
	
	public int totalPlayers;
	public int totalSleeping;
	public float totalPercent;
	
	static double sleepPercent;
	static int groundLevel;
	static String onSleepText;
	static String onWakeText;
	static String onMorningText;
	static boolean alertEnabled;
	static boolean includeMiners;
	
	public void onEnable()
	{
		loadConfig();
		new EventListener(this);
	}
	
	public void onDisable()
	{
		
	}
	
	public void setDay()
	{
		World world = org.bukkit.Bukkit.getWorld("world");
		world.setTime(1000L);
		if (world.hasStorm())
		{
			world.setWeatherDuration(0);
			world.setThunderDuration(0);
			world.setThundering(false);
		}
	}
	
	public void doSleep(final Player player)
	{
		sleepDelayPlayers.add(player);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			public void run()
			{
				if (sleepDelayPlayers.contains(player))
				{
					sleepDelayPlayers.remove(player);
					sleepPlayers.add(player);
					testSleep(player);
				}
			}
		}, 100L);
	}
	
	public void testSleep(Player player)
	{
		getServer().broadcastMessage(player.getDisplayName() + " " + ChatColor.GOLD + SleepPercent.onSleepText + " " + getSleepData());
		
		if (totalPercent >= (float)sleepPercent)
		{
			sleepPlayers.clear();
			setDay();
			getServer().broadcastMessage(ChatColor.GOLD + SleepPercent.onMorningText);
		}
	}
	
	public void testSleep()
	{
		getSleepData();
		if (totalPercent >= (float)sleepPercent)
		{
			sleepPlayers.clear();
			setDay();
			getServer().broadcastMessage(ChatColor.GOLD + SleepPercent.onMorningText);
		}
	}
	
	public String getSleepData()
	{
		totalSleeping = sleepPlayers.size();
		totalPlayers = getTotalPlayers();
		totalPercent = new Float(totalSleeping).floatValue() / new Float(totalPlayers).floatValue() * new Float(100.0F).floatValue();
		totalPercent = Math.round(totalPercent * new Float(100.0F).floatValue()) / new Float(100.0F).floatValue();
		return totalSleeping + "/" + totalPlayers + " (" + totalPercent + "%)";
	}
	
	public int getTotalPlayers()
	{
		int totalPlayers = 0;
		int miningPlayers = 0;
		for(Player player : getServer().getOnlinePlayers())
		{
			if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL))
			{
				if (player.getLocation().getY() < SleepPercent.groundLevel)
					miningPlayers++;
				totalPlayers++;
			}
		}
		return SleepPercent.includeMiners ? totalPlayers : totalPlayers - miningPlayers;
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		SleepPercent.sleepPercent = getConfig().getDouble("percent");
		SleepPercent.groundLevel = getConfig().getInt("groundLevel");
		SleepPercent.alertEnabled = getConfig().getBoolean("alertEnabled");
		SleepPercent.includeMiners = getConfig().getBoolean("includeMiners");
		SleepPercent.onSleepText = getConfig().getString("alerts.onSleepText");
		SleepPercent.onWakeText = getConfig().getString("alerts.onWakeText");
		SleepPercent.onMorningText = getConfig().getString("alerts.onMorningText");
	}
}
