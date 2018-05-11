package com.elixermc.prison.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.elixermc.prison.Core;
import com.elixermc.prison.events.InventoryClick;
import com.elixermc.prison.managers.ConfigManager;

public class DeleteMine implements CommandExecutor {
	
	Core plugin = Core.getInstance();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	ConfigManager cm = null;
	InventoryClick ic = null;
	

	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] a) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (ic == null) {
			ic = new InventoryClick();
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a 'Player' to run this command!");
			return true;
		}
		Player p = (Player) sender;
		int length = a.length;
		if (cmd.getName().equalsIgnoreCase("prdelmine")) {
			if (!p.hasPermission("prisonremade.command.delmine")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou must define a name. &6Correct Usage: /prdeletemine <name>"));
				return true;
			} else if (length == 1) {
				String name = a[0];
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine has already been deleted or hasn't been created!"));
					return true;
				}
				// TODO Saves mine name temp. to config
				plugin.getConfig().set("temp.minename", name);
				plugin.saveConfig();
				sendConfirmation(p,name);
				return true;
			} else if (length >= 2) {
				p.sendMessage(format(prefix+"&cToo many arguments! &6Correct Usage: /prdeletemine <name>"));
				return true;
			}
		}
		return true;
	}
	public void sendConfirmation(Player p, String name) {
		Inventory inv = Bukkit.createInventory(null, 27,format("&2&lAre you sure? (Mine Delete)"));
		//
		ItemStack base = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)8);
		ItemStack yes = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)5);
		ItemStack no = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14);
		ItemStack mine = new ItemStack(Material.STONE,1);
		//
		List<Integer> skipslots = new ArrayList<Integer>();
		skipslots.add(10);
		skipslots.add(13);
		skipslots.add(16);
		setBase(inv,base,skipslots);
		ArrayList<String> yesl = new ArrayList<String>();
		ArrayList<String> nol = new ArrayList<String>();
		ArrayList<String> minel = new ArrayList<String>();
		yesl.add(format("&7By clicking this, you are confirming to delete &e"+name));
		nol.add(format("&7By clicking this, you are not going to delete the mine."));
		minel.add(format("&7The mine your deleting."));
		setIm(yes,"&2CONFIRM",yesl);
		setIm(no,"&cCANCEL",nol);
		setIm(mine,"&aYou are deleting &e"+name,minel);
		//
		inv.setItem(10, yes);
		inv.setItem(13, mine);
		inv.setItem(16, no);
		//
		p.openInventory(inv);
		//
		return;
	}
	public void setIm(ItemStack is, String dn, ArrayList<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(format(dn));
		im.setLore(lore);
		is.setItemMeta(im);
		return;
	}
	public void setBase(Inventory inv, ItemStack base, List<Integer> skipslots) {
		ArrayList<String> lore = new ArrayList<String>();
		setIm(base,"&7",lore);
		int max = inv.getSize();
		int id = 0;
		for (id = 0;id<max;id++) {
			if (id == max) {
				break;
			}
			if (skipslots.contains(id)) {
				continue;
			}
			inv.setItem(id, base);
		}
		return;
	}
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
