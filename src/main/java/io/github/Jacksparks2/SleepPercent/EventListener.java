package io.github.Jacksparks2.SleepPercent;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class EventListener implements Listener
{
	SleepPercent sleepPercent;
	
	public EventListener(SleepPercent plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		sleepPercent = plugin;
	}
	
	@EventHandler
	public void onBedEnter(final PlayerBedEnterEvent event)
	{
		event.getPlayer().getServer().getScheduler().scheduleSyncDelayedTask(sleepPercent, new Runnable() 
		{
			public void run()
			{
				sleepPercent.sleepPlayers.add(event.getPlayer());
				if (sleepPercent.getServer().getOnlinePlayers().size() >= 1)
					sleepPercent.testSleep();
			}
		}, 100L);
	}
	
	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event)
	{
		sleepPercent.sleepPlayers.remove(event.getPlayer());
		if (org.bukkit.Bukkit.getWorld("world").getTime() >= 12000L)
			sleepPercent.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " " + ChatColor.GOLD + SleepPercent.onWakeText);
	}
}