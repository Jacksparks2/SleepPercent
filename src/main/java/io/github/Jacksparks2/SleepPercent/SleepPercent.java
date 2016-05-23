package io.github.Jacksparks2.SleepPercent;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SleepPercent extends JavaPlugin
{
	public List<Player> sleepPlayers = new java.util.ArrayList<Player>();
	public int miningPlayers;
	
	static int sleepPercent;
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
	
	public void testSleep()
	{
		int totalPlayers = getServer().getOnlinePlayers().size() - getMiningPlayers();
		int totalSleeping = this.sleepPlayers.size();
		
		float fTotalPlayers = new Float(totalPlayers).floatValue();
		float fTotalSleeping = new Float(totalSleeping).floatValue();
		
		float percent = new Float(totalSleeping / (totalPlayers - getMiningPlayers()) * 100.0F).floatValue();
		
		getServer().broadcastMessage(((Player)this.sleepPlayers.get(this.sleepPlayers.size() - 1)).getDisplayName() + " " + ChatColor.GOLD + SleepPercent.onSleepText + " " + totalSleeping + "/" + totalPlayers + " (" + fTotalSleeping / fTotalPlayers * 100.0F + "%)");
		
		if (percent >= new Float(SleepPercent.sleepPercent).floatValue())
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
			getServer().broadcastMessage(ChatColor.GOLD + SleepPercent.onMorningText);
		}
	}
	
	public int getMiningPlayers()
	{
		int miningPlayers = 0;
		for(Player player : getServer().getOnlinePlayers())
		{
			if (player.getLocation().getY() < SleepPercent.groundLevel)
				miningPlayers++;
		}
		return !SleepPercent.includeMiners ? miningPlayers : 0;
	}
	
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		SleepPercent.sleepPercent = getConfig().getInt("percent");
		SleepPercent.groundLevel = getConfig().getInt("groundLevel");
		SleepPercent.alertEnabled = getConfig().getBoolean("alertEnabled");
		SleepPercent.includeMiners = getConfig().getBoolean("includeMiners");
		SleepPercent.onSleepText = getConfig().getString("alerts.onSleepText");
		SleepPercent.onWakeText = getConfig().getString("alerts.onWakeText");
		SleepPercent.onMorningText = getConfig().getString("alerts.onMorningText");
	}
}
