package com.elixermc.prison.managers;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;

public class MineManager {
	
	Core plugin = Core.getInstance();
	ConfigManager cm = null;
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	MessageManager mm = null;
	
	public Location getCorner1(String name) {
		int x = plugin.getConfig().getInt("Mines."+name+".location.corner1.x");
		int y = plugin.getConfig().getInt("Mines."+name+".location.corner1.y");
		int z = plugin.getConfig().getInt("Mines."+name+".location.corner1.z");
		World w = Bukkit.getWorld(plugin.getConfig().getString("Mines."+name+".location.corner1.world"));
		Location loc = new Location(w,x,y,z);
		return loc;
	}
    public Location getCorner2(String name) {
    	int x = plugin.getConfig().getInt("Mines."+name+".location.corner2.x");
		int y = plugin.getConfig().getInt("Mines."+name+".location.corner2.y");
		int z = plugin.getConfig().getInt("Mines."+name+".location.corner2.z");
		World w = Bukkit.getWorld(plugin.getConfig().getString("Mines."+name+".location.corner2.world"));
		Location loc = new Location(w,x,y,z);
		return loc;
	}
    public int getResetDelay(String name) {
    	int delay = plugin.getConfig().getInt("Mines."+name+".resetdelay");
    	return delay;
    }
    public int getResetTimer(String name) {
    	int timer = plugin.getConfig().getInt("Mines."+name+".resettimer");
    	return timer;
    }
    public void setResetTimer(String name, int timer) {
    	plugin.getConfig().set("Mines."+name+".resettimer", timer);
    	plugin.saveConfig();
    	return;
    }  
    public void reduceTimer(String name) {
    	int timer = getResetTimer(name);
    	timer--;
    	setResetTimer(name,timer);
    	if (isReadyToReset(name)) {
    		resetMineAuto(name);
    		return;
    	} else {
    		return;
    	}
    }
    public boolean isReadyToReset(String name) {
    	int delay = getResetDelay(name);
    	int timer = getResetTimer(name);
    	if (timer == 0) {
    		return true;
    	} else if (timer != 0) {
    		return false;
    	} else if (timer <= -1) {
    		setResetTimer(name,delay);
    		return true;
    	}
    	return false;
    }
    public List<String> getBlocks(String name) {
    	List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
    	Bukkit.getConsoleSender().sendMessage(""+blocks.get(0));
    	return blocks;
    }
    @SuppressWarnings("deprecation")
	public void setBlockType(Location l, String m, byte d) {
    	Material mat = Material.getMaterial(Integer.parseInt(m));
    	Bukkit.getConsoleSender().sendMessage(""+mat.name());
    	Block b = l.getBlock();
    	b.setType(mat);
    	b.setData(d);
    	return;
    }
	public void resetMineAuto(String name) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (mm == null) {
			mm = new MessageManager();
		}
		if (!cm.isAutoResetOn(name)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.isOp() || p.hasPermission("prisonremade.announcement.autoresetoff")) {
					p.sendMessage(format(prefix+"&eMine &a"+name+" &ehasn't be auto reset due to the fact its turned off."));
					continue;
				} 
			}
			return;
		}
		Location corner1 = getCorner1(name);
		Location corner2 = getCorner2(name);
		int minX = corner1.getBlockX();
		int minY = corner1.getBlockY();
		int minZ = corner1.getBlockZ();
		int maxX = corner2.getBlockX();
		int maxY = corner2.getBlockY();
		int maxZ = corner2.getBlockZ();
		Random rand = new Random();
		for (int x = minX;x <= maxX;x++) {
			for (int y = minY;y <= maxY;y++) {
				for (int z = minZ;z <= maxZ;z++) {
					List<String> info = getBlocks(name);
					int block = rand.nextInt(info.size());
					String[] split = info.get(block).split(",");
					String material = split[0];
					byte data = Byte.parseByte(split[1]);
					Location l = new Location(corner1.getWorld(),x,y,z);	
					setBlockType(l,material,data);
				}
			}
		}
		setResetTimer(name,getResetDelay(name));
		mm.sendAutoResetMineMessage(name);
	}
	public void resetMine(String name) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (mm == null) {
			mm = new MessageManager();
		}
		Location corner1 = getCorner1(name);
		Location corner2 = getCorner2(name);
		int minX = corner1.getBlockX();
		int minY = corner1.getBlockY();
		int minZ = corner1.getBlockZ();
		int maxX = corner2.getBlockX();
		int maxY = corner2.getBlockY();
		int maxZ = corner2.getBlockZ();
		Random rand = new Random();
		for (int x = minX;x <= maxX;x++) {
			for (int y = minY;y <= maxY;y++) {
				for (int z = minZ;z <= maxZ;z++) {
					List<String> info = getBlocks(name);
					int block = rand.nextInt(info.size());
					Bukkit.getConsoleSender().sendMessage(""+block);
					Bukkit.getConsoleSender().sendMessage(""+info.get(block));
					String[] split = info.get(block).split(",");
					String material = split[0];
					byte data = Byte.parseByte(split[1]);
					Location l = new Location(corner1.getWorld(),x,y,z);	
					setBlockType(l,material,data);
				}
			}
		}
		setResetTimer(name,getResetDelay(name));
		mm.sendResetMineMessage(name);
	}
	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
