package com.elixermc.prison.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;

public class ConfigManager {
	
	Core plugin = Core.getInstance();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	MessageManager mm = null;
	int id = 0;
	// TODO MINE METHODS
	
	public void createNewMine(Player p, String name,Location corner1, Location corner2) {
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
		blocks.add("0,0");
		plugin.getConfig().set("Mines."+name+".blocks", blocks);
		plugin.getConfig().set("Mines."+name+".silentreset", false);
		plugin.getConfig().set("Mines."+name+".resetdelay", 5);
		plugin.getConfig().set("Mines."+name+".autoreset", true);
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
	// TODO BLOCK METHODS
	public void addBlock(Player p,String name, String block) {
		if (mm == null) {
			mm = new MessageManager();
		}
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		if (blocks.contains("0,0")) {
			blocks.remove(0);
		}
		blocks.add(block);
		plugin.getConfig().set("Mines."+name+".blocks", blocks);
		plugin.saveConfig();
		String[] split = block.split(",");
		mm.sendAddedBlockMessage(p, name, split[0], Byte.parseByte(split[1]));
		return;
	}
	public void removeBlock(Player p, String name, int id) {
		if (mm == null) {
			mm = new MessageManager();
		}
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		String block = blocks.get(id-1);
		mm.sendRemovedBlockMessage(p,name,block);
		blocks.remove(id-1);
		plugin.getConfig().set("Mines."+name+".blocks", blocks);
		plugin.saveConfig();
		return;
	}
	@SuppressWarnings("deprecation")
	public void listBlocks(Player p,String name) {
		if (prefix == null) {
			prefix = plugin.getConfig().getString("Settings.prefix");
		}
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		int blockSize = blocks.size();
		if (blockSize == 0) {
			p.sendMessage(format(prefix+"&cThere are no blocks in the mine."));
			return;
		}
		int count = 0;
		int start = 0 * 5;
		int end = start + 5;
		p.sendMessage(format("&c-= [ &aBlock list for &e"+name+" &aPage 1 &c] =-"));
		for (int i = 0;i<blockSize;i++) {
			if (count >= start && count < end) {
				String block = blocks.get(i);
				String[] fblock = block.split(",");
				Material mat = Material.getMaterial(Integer.parseInt(fblock[0]));
				byte data = Byte.parseByte(fblock[1]);
				String message = String.format("&b"+(count+1)+". &e%s&a (&e%s &awith data &e%s&a)", block,mat.name(),data);
				p.sendMessage(format(message));
			}
			count++;
		}
		if (end < blockSize) {
			p.sendMessage(format(prefix+"&aDo /prlistblock 2 for more blocks."));
			return;
		} else {
			return;
		}
	}
	@SuppressWarnings("deprecation")
	public void listBlocks(Player p,String name,int page) {
		if (prefix == null) {
			prefix = plugin.getConfig().getString("Settings.prefix");
		}
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		int blockSize = blocks.size();
		if (blockSize == 0) {
			p.sendMessage(format(prefix+"&cThere are no blocks in the mine."));
			return;
		}
		int start = (page-1) * 5;
		int end = start + 5;
		int count = start;
		p.sendMessage(format("&c-= [ &aBlock list for &e"+name+" &aPage "+page+" &c] =-"));
		for (int i = 0;i<blockSize;i++) {
			if (count >= start && count < end) {
				String block = blocks.get(i);
				String[] fblock = block.split(",");
				Material mat = Material.getMaterial(Integer.parseInt(fblock[0]));
				byte data = Byte.parseByte(fblock[1]);
				String message = String.format("&b"+(count+1)+". &e%s&a (&e%s &awith data &e%s&a)", block,mat.name(),data);
				p.sendMessage(format(message));
			}
			count++;
		}
		if (end < blockSize) {
			p.sendMessage(format(prefix+"&aDo /prlistblock "+(page+1)+" for more blocks."));
			return;
		} else {
			return;
		}
	}
	public boolean doesBlockExist(String name, String block) {
		List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
		if (blocks.contains(block)) {
			return true;
		} else {
			return false;
		}
	}
	public boolean blockIdAvailable(String name, int id) {
		int blocks = plugin.getConfig().getStringList("Mines."+name+".blocks").size();
		if (blocks == 0) {
			return false;
		}
		if (id > blocks || id < blocks) {
			return false;
		} else {
			return true;
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
		plugin.getConfig().set("Mines."+name+".resettimer", Integer.parseInt(delay));
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
	// TODO SILENT METHODS
	public boolean isSilent(String name) {
		if (plugin.getConfig().getBoolean("Mines."+name+".silentreset")) {
			return true;
		} else {
			return false;
		}
	}
	public void setSilentMode(Player p,String name) {
		if (mm == null) {
			mm = new MessageManager();
		}
		boolean b = isSilent(name);
		if (b) {
			b = false;
			plugin.getConfig().set("Mines."+name+".silentreset", b);
			plugin.saveConfig();
			mm.sendSetSilentModeMessage(p, name, b);
			return;
		} else {
			b = true;
			plugin.getConfig().set("Mines."+name+".silentreset", b);
			plugin.saveConfig();
			mm.sendSetSilentModeMessage(p, name, b);
			return;
		}
		
	}
	// TODO OTHER METHODS
	public boolean doesMineExist(String name) {
		if (plugin.getConfig().getString("Mines."+name) != null) {
			return true;
		} else {
			return false;
		}
	}
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&',s);
	}
	
}
