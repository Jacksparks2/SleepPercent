package io.github.Jacksparks2.SleepPercent;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		sleepPercent.doSleep(event.getPlayer());
	}
	
	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event)
	{
		if (sleepPercent.sleepPlayers.contains(event.getPlayer()))
		{
			sleepPercent.sleepPlayers.remove(event.getPlayer());
			if (org.bukkit.Bukkit.getWorld("world").getTime() >= 12000L)
				sleepPercent.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " " + ChatColor.GOLD + SleepPercent.onWakeText);
		}
		if (sleepPercent.sleepDelayPlayers.contains(event.getPlayer()))
		{
			sleepPercent.sleepDelayPlayers.remove(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (sleepPercent.sleepPlayers.contains(event.getPlayer()))
		{
			sleepPercent.sleepPlayers.remove(event.getPlayer());
			if (org.bukkit.Bukkit.getWorld("world").getTime() >= 12000L)
				sleepPercent.getServer().broadcastMessage(event.getPlayer().getDisplayName() + " " + ChatColor.GOLD + SleepPercent.onWakeText);
		}
		if (sleepPercent.sleepDelayPlayers.contains(event.getPlayer()))
		{
			sleepPercent.sleepDelayPlayers.remove(event.getPlayer());
		}
		sleepPercent.testSleep();
	}
}