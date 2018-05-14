package com.elixermc.prison.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
    public HashMap<String, Integer> getBlockChance(String name) {
    	List<String> blocks = plugin.getConfig().getStringList("Mines."+name+".blocks");
    	HashMap<String,Integer> info = new HashMap<String,Integer>();
    	Random ran = new Random();
    	int area = plugin.getConfig().getInt("Mines."+name+".area");
    	for (int count = 0;count<area;count++) {
    		int index = ran.nextInt(blocks.size());
    		String block = blocks.get(index);
    		if (info.containsKey(block)) {
    			int num = info.get(block);
    			Bukkit.getConsoleSender().sendMessage(""+num+","+count);
    			info.put(block,num+1);
    		} else {
    			info.put(block,1);
    		}
    	}
    	
    	return info;
    }
    @SuppressWarnings("deprecation")
	public void setBlockType(Location l, String m, byte d) {
    	Material mat = Material.getMaterial(Integer.parseInt(m));
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
					int r = rand.nextInt(100)+1;
					HashMap<String,Integer> info = getBlockChance(name);
					Location l = new Location(corner1.getWorld(),x,y,z);
					for (Entry<String, Integer> set : info.entrySet()) {
						String[] block = set.getKey().split(":");
						int count = set.getValue();
						String mat = block[0];
						byte data = Byte.parseByte(block[1]);
						int chance = Integer.parseInt(block[2]);
						if (count == 0) {
							info.remove(set.getKey());
							continue;
						}
						if (r <= chance) {
							setBlockType(l,mat,data);
							count--;
							set.setValue(count);
							break;
						}
					}	
				}
				continue;
			}
			continue;
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
		Bukkit.getConsoleSender().sendMessage(""+corner1.getBlockX()+","+corner1.getBlockY()+","+corner1.getBlockZ()+","+corner1.getWorld().getName());
		Bukkit.getConsoleSender().sendMessage(""+corner2);
		Random rand = new Random();
		for (int x = corner1.getBlockX();x <= corner2.getBlockX();x++) {
			for (int y = corner1.getBlockY();y <= corner2.getBlockY();y++) {
				for (int z = corner1.getBlockZ();z <= corner2.getBlockZ();z++) {
					int r = rand.nextInt(100)+1;
					HashMap<String,Integer> info = getBlockChance(name);
					Location l = new Location(corner1.getWorld(),x,y,z);
					for (Entry<String, Integer> set : info.entrySet()) {
						String[] block = set.getKey().split(":");
						int count = set.getValue();
						String mat = block[0];
						byte data = Byte.parseByte(block[1]);
						int chance = Integer.parseInt(block[2]);
						if (count == 0) {
							info.remove(set.getKey());
							continue;
						}
						if (r <= chance) {
							setBlockType(l,mat,data);
							count -= count;
							set.setValue(count);
							break;
						}
					}
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
