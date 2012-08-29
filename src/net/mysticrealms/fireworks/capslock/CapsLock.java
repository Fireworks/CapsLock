package net.mysticrealms.fireworks.capslock;
import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class CapsLock extends JavaPlugin {

	public Configuration config;

	public boolean enableNoCaps, enableMessageOverride, enablePlayerMessage,
			enableForceLowercase;
	public String overrideMessage, playerMessage;
	public List<String> whitelist;
	public int capsInRow, totalCapsPercentage;

	@Override
	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(new CapsLockListener(this), this);

		if (!loadConfig()) {
			getLogger().severe("Something is wrong with the config! Disabling!");
			setEnabled(false);
			return;
		}

		if (!enableNoCaps) {
			getLogger().severe("StopCaps set to disabled in config. Disabling!");
			setEnabled(false);
			return;
		}
	}

	public boolean loadConfig() {

		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}

		reloadConfig();
		config = getConfig();

		if (config.isBoolean("enableNoCaps")) {
			enableNoCaps = config.getBoolean("enableNoCaps");
		} else {
			return false;
		}

		if (config.isBoolean("enableMessageOverride")) {
			enableMessageOverride = config.getBoolean("enableMessageOverride");
		} else {
			return false;
		}

		if (config.isBoolean("enablePlayerMessage")) {
			enablePlayerMessage = config.getBoolean("enablePlayerMessage");
		} else {
			return false;
		}

		if (config.isBoolean("enableForceLowercase")) {
			enableForceLowercase = config.getBoolean("enableForceLowercase");
		} else {
			return false;
		}

		if (config.isString("overrideMessage")) {
			overrideMessage = config.getString("overrideMessage");
		} else {
			return false;
		}

		if (config.isString("playerMessage")) {
			playerMessage = config.getString("playerMessage");
		} else {
			return false;
		}

		if (config.isInt("capsInRow")) {
			capsInRow = config.getInt("capsInRow");
		} else {
			return false;
		}

		if (config.isInt("totalCapsPercentage") && config.getInt("totalCapsPercentage") >= 0) {
			totalCapsPercentage = config.getInt("totalCapsPercentage");
		} else {
			return false;
		}

		if (config.isList("whitelist")) {
			whitelist = config.getStringList("whitelist");
		} else {
			return false;
		}

		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("capslock") && args[0].equalsIgnoreCase("reload")) {
			if (loadConfig()) {
				sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
			} else {
				sender.sendMessage(ChatColor.GOLD + "Config failed to reload!");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("capslock") && args[0].equalsIgnoreCase("toggle")){
			enableNoCaps = !enableNoCaps;
			if (enableNoCaps){
				sender.sendMessage(ChatColor.GOLD + "CapsLock now enabled.");
			}else{
				sender.sendMessage(ChatColor.GOLD + "CapsLock now disabled.");
			}
		}
		return true;
	}
}