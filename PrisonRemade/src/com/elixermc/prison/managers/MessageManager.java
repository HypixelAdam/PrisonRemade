package com.elixermc.prison.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;

public class MessageManager {
	
	Core plugin = Core.getInstance();
	ConfigManager cm = new ConfigManager();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	
	public void sendNewMineMessage(Player p,String name, List<Integer> corner1, List<Integer> corner2) {
		int x1 = corner1.get(0);
		int y1 = corner1.get(1);
		int z1 = corner1.get(2);
		int x2 = corner2.get(0);
		int y2 = corner2.get(1);
		int z2 = corner2.get(2);
		String message = String.format("&aMine created with name &e%s&a.", name);
		String locations = String.format("&aLocations - Corner 1: &e%s&a,&e%s&a,&e%s &aCorner 2: &e%s&a,&e%s&a,&e%s&a", x1,y1,z1,x2,y2,z2);
		p.sendMessage(format(prefix+message));
		p.sendMessage(format(prefix+locations));
		return;
	}
	public void sendSetAutoResetMessage(Player p, String name, boolean b) {
		String message = String.format("&aYou have set the auto reset for &e%s &ato &e%s&a.", name,b);
		p.sendMessage(format(prefix+message));
		return;
	}
	public void sendDeleteMineMessage(Player p,String name) {
		String message = String.format("&e%s &ahas been deleted.", name);
		p.sendMessage(format(prefix+message));
		return;
	}
	public void sendDidNotDeleteMessage(Player p, String name) {
		String message = String.format("&aYou have decided not to delete &e%s&a.",name);
		p.sendMessage(format(prefix+message));
		return;
	}
	public void sendSetResetDelayMessage(Player p, String name, String delay) {
		String message = String.format("&aYou have set the reset delay of &e%s &ato &e%s&a minutes.", name,delay);
		p.sendMessage(format(prefix+message));
		return;
	}
	public void sendResetMineMessage(String name) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (cm.isSilent(name)) {
			return;
		}
		String message = String.format("&aMine &e%s &ahas been reset.", name);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(format(prefix+message));
		}
		return;
	}
	public void sendAutoResetMineMessage(String name) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (cm.isSilent(name)) {
			return;
		}
		String message = String.format("&aMine &e%s &ahas been auto reset.", name);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(format(prefix+message));
		}
		return;
	}
	public void sendAddedBlockMessage(Player p, String name, String block, byte data, int chance) {
		String message = String.format("&aAdded block &e%s&a:&e%s with a chance of &e%s&a.", block,data,chance);
		p.sendMessage(format(prefix+message));
		return;
	}
	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
