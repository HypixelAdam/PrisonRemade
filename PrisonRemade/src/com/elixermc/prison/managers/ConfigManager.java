package com.elixermc.prison.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;

public class ConfigManager {
	
	Core plugin = Core.getInstance();
	String prefix;
	MessageManager mm = null;
	int id = 0;
	// TODO MINE METHODS
	
	public void createNewMine(Player p, String name,Location corner1, Location corner2, int area) {
		if (mm == null) {
			mm = new MessageManager();
		}
		if (prefix == null) {
			prefix = plugin.getConfig().getString("Settings.prefix");
		}
		if (doesMineExist(name)) {
			p.sendMessage(format(prefix+"&cThis mine already exists, either delete it or redefine it!."));
			return;
		}
		id = getNewMineId();
		List<Integer> corner1b = saveCorner1Location(name,corner1);
		List<Integer> corner2b = saveCorner2Location(name,corner2);
		saveId(name);
		List<String> blocks = new ArrayList<String>();
		blocks.add("0:0");
		plugin.getConfig().set("Mines."+name+".blocks", blocks);
		plugin.getConfig().set("Mines."+name+".silentreset", false);
		plugin.getConfig().set("Mines."+name+".resetdelay", 5);
		plugin.getConfig().set("Mines."+name+".autoreset", true);
		plugin.getConfig().set("Mines."+name+".area", area);
		plugin.saveConfig();
		mm.sendNewMineMessage(p, name, corner1b, corner2b);
		return;
	}
	
	public void deleteMine(Player p, String name) {
		if (mm == null) {
			mm = new MessageManager();
		}
		plugin.getConfig().set("Mines."+name, null);
		plugin.saveConfig();
		mm.sendDeleteMineMessage(p, name);
		return;
	}
	
	public List<Integer> saveCorner1Location(String name, Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String w = loc.getWorld().getName();
		plugin.getConfig().set("Mines."+name+".location.corner1.x", x);
		plugin.getConfig().set("Mines."+name+".location.corner1.y", y);
		plugin.getConfig().set("Mines."+name+".location.corner1.z", z);
		plugin.getConfig().set("Mines."+name+".location.corner1.world", w);
		plugin.saveConfig();
		List<Integer> intblocks = new ArrayList<Integer>();
		intblocks.add(x); intblocks.add(y); intblocks.add(z);
		return intblocks;
	}
	
	public List<Integer> saveCorner2Location(String name, Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String w = loc.getWorld().getName();
		plugin.getConfig().set("Mines."+name+".location.corner2.x", x);
		plugin.getConfig().set("Mines."+name+".location.corner2.y", y);
		plugin.getConfig().set("Mines."+name+".location.corner2.z", z);
		plugin.getConfig().set("Mines."+name+".location.corner2.world", w);
		plugin.saveConfig();
		List<Integer> intblocks = new ArrayList<Integer>();
		intblocks.add(x); intblocks.add(y); intblocks.add(z);
		return intblocks;
	}
	// TODO ADD BLOCK METHODS
	public void addBlock(String name, String block) {
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		blocks.add(block);
		plugin.getConfig().set("Mines."+name+".blocks", blocks);
		plugin.saveConfig();
		return;
	}
	public void removeBlock(String name, int id) {
		
	}
	public void listBlocks(Player p,String name) {
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		int blockSize = blocks.size();
		int count = 0;
		
		int start = 0 * 5;
		int end = start + 5;
		
		for (int i = 0;i<blockSize;i++) {
			if (count >= start && count < end) {
				
			}
		}
	}
	public void listBlocks(String name,int page) {
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		for (int i = 0;i<blocks.size();i++) {
			
		}
	}
	// TODO AUTO RESET METHODS
	public boolean isAutoResetOn(String name) {
		boolean b = plugin.getConfig().getBoolean("Mines."+name+".autoreset");
		if (b) {return true;} else {return false;}
	}
	
	public void setAutoReset(Player p,String name, boolean b) {
		if (mm == null) {
			mm = new MessageManager();
		}
		plugin.getConfig().set("Mines."+name+".autoreset", b);
		plugin.saveConfig();
		mm.sendSetAutoResetMessage(p, name, b);
		return;
	}
	
	public void setResetDelay(Player p, String name, String delay) {
		if (mm == null) {
			mm = new MessageManager();
		}
		plugin.getConfig().set("Mines."+name+".resetdelay", Integer.parseInt(delay));
		plugin.saveConfig();
		mm.sendSetResetDelayMessage(p,name,delay);
		return;
	}
	// TODO ID METHODS
	public void saveId(String name) {
		plugin.getConfig().set("Mines."+name+".id", id);
		plugin.saveConfig();
		return;
	}
	
	public int getNewMineId() {
		id = 0;
		for (String name : plugin.getConfig().getConfigurationSection("Mines").getKeys(false)) {
			int mid = Integer.parseInt(plugin.getConfig().getString("Mines."+name+".id"));
			if (id == mid) {
				id++;
				continue;
			} else {
				break;
			}
		}
		return id;
	}
	
	// TODO OTHER METHODS
	public boolean doesMineExist(String name) {
		if (plugin.getConfig().getString("Mines."+name) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSilent(String name) {
		if (plugin.getConfig().getBoolean("Mines."+name+".silentreset")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&',s);
	}
	
}
