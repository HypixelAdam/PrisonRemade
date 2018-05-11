package com.elixermc.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;
import com.elixermc.prison.managers.ConfigManager;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class NewMine implements CommandExecutor {
	
	Core plugin = Core.getInstance();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	ConfigManager cm = new ConfigManager();

	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] a) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a 'Player' to run this command!");
			return true;
		}
		Player p = (Player) sender;
		int length = a.length;
		if (cmd.getName().equalsIgnoreCase("prnewmine")) {
			if (!p.hasPermission("prisonremade.command.newmine")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou need to define a name. &6Correct Usage: /prnewmine <name> OR /prnm <name>"));
				return true;
			} else if (length == 1) {
				String name = a[0];
				Selection s = plugin.getWorldEdit().getSelection(p);
				if (s == null) {
					p.sendMessage(format(prefix+"&cYour selection is null."));
					return true;
				}
				cm.createNewMine(p, name, s.getMinimumPoint(), s.getMaximumPoint(), s.getArea());
				return true;
			} else if (length >= 2) {
				p.sendMessage(format(prefix+"&cToo many arguments! &6Correct Usage: /prnewmine <name> OR /prnm <name>"));
				return true;
			}
		}
		return true;
	}

	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
