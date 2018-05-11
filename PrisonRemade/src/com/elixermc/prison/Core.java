package com.elixermc.prison;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.elixermc.prison.commands.DeleteMine;
import com.elixermc.prison.commands.NewMine;
import com.elixermc.prison.commands.ResetMine;
import com.elixermc.prison.commands.SetResetDelay;
import com.elixermc.prison.commands.ToggleAutoReset;
import com.elixermc.prison.events.InventoryClick;
import com.elixermc.prison.managers.MineManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Core extends JavaPlugin {
	
	private static Core plugin;
	MineManager mim = null;
	File configFile;
	int scheid = 0;

	public void onEnable() {
		plugin = this;
		registerEvents();
		if (mim == null) {
			mim = new MineManager();
		}
		scheid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (String mine : getConfig().getConfigurationSection("Mines").getKeys(false)) {
					if (mine == null) {
						continue;
					}
					mim.reduceTimer(mine);
				}
			}
		}, 1200L, 1200L);
	}

	public void onDisable() {
		plugin = null;
	}

	public void registerEvents() {
		setupConfig();
		saveDefaultConfig();
		// ADD COMMANDS HERE
		getCmds("prnewmine",new NewMine());
		getCmds("prdelmine",new DeleteMine());
		getCmds("prsetresetdelay",new SetResetDelay());
		getCmds("prresetmine",new ResetMine());
		getCmds("prtoggleautoreset",new ToggleAutoReset());
		// ADD LISTENERS HERE
		getLisn(new InventoryClick(),this);
	}
	
	public void setupConfig() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		configFile = new File(getDataFolder(), "config.yml");
		loadConfig(configFile,"config.yml");
	}
	public void loadConfig(File f, String filename) {
		InputStream in = getResource(filename);
	    if(f.exists()) {
	    	return;
	    }
	    try {
			Files.copy(in, f.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getCmds(String command, CommandExecutor cmdclass) {
		getCommand(command).setExecutor(cmdclass);
		return;
	}
	public WorldEditPlugin getWorldEdit() {
		Plugin we = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (we instanceof WorldEditPlugin) {
			return (WorldEditPlugin) we;
		} else {
			return null;
		}
	}
	public void getLisn(Listener l, Plugin p) {
		Bukkit.getServer().getPluginManager().registerEvents(l, p);
	}

	public static Core getInstance() {
		return plugin;
	}
}
