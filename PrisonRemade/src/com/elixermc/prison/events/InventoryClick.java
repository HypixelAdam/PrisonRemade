package com.elixermc.prison.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.elixermc.prison.Core;
import com.elixermc.prison.commands.DeleteMine;
import com.elixermc.prison.managers.ConfigManager;
import com.elixermc.prison.managers.MessageManager;

public class InventoryClick implements Listener {
	
	Core plugin = Core.getInstance();
	DeleteMine dm = new DeleteMine();
	ConfigManager cm = new ConfigManager();
	MessageManager mm = new MessageManager();
	String minedel = null;
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if (inv.getType() == InventoryType.CREATIVE) {
			return;
		}
		if (inv.getName().equals(format("&2&lAre you sure? (Mine Delete)"))) {
			minedel = plugin.getConfig().getString("temp.minename");
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(format("&7"))) {
				e.setCancelled(true);
				return;
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(format("&2CONFIRM"))) {
				e.setCancelled(true);
				cm.deleteMine(p, minedel);
				p.closeInventory();
				minedel = null;
				plugin.getConfig().set("temp", null);
				plugin.saveConfig();
				return;
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(format("&cCANCEL"))) {
				e.setCancelled(true);
				mm.sendDidNotDeleteMessage(p, minedel);
				p.closeInventory();
				minedel = null;
				plugin.getConfig().set("temp", null);
				plugin.saveConfig();
				return;
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(format("&aYou are deleting &e"+minedel))) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
