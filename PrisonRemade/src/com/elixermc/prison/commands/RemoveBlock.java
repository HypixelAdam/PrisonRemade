package com.elixermc.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;
import com.elixermc.prison.managers.ConfigManager;

public class RemoveBlock implements CommandExecutor {
	
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
		if (cmd.getName().equalsIgnoreCase("prremoveblock")) {
			if (!p.hasPermission("prisonremade.command.removeblock")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou must define a name. &6Correct Usage: /prremoveblock <name> <id> OR /prrb <name> <id>"));
				return true;
			} else if (length == 1) {
				p.sendMessage(format(prefix+"&cYou must define a id. &6Correct Usage: /prremoveblock <name> <id> OR /prrb <name> <id>"));
				return true;
			} else if (length == 2) {
				String name = a[0];
				int id = Integer.parseInt(a[1]);
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine doesn't exist!"));
					return true;
				}
				if (!cm.blockIdAvailable(name, id)) {
					p.sendMessage(format(prefix+"&cThat block id in this mine is not available!"));
					return true;
				}
				cm.removeBlock(p, name, id);
				return true;
			} else if (length >= 3) {
				p.sendMessage(format(prefix+"&cToo many arguments! &6Correct Usage: /prremoveblock <name> <id> OR /prrb <name> <id>"));
			}
		}
		return true;
	}

	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
