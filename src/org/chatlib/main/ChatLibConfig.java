package org.chatlib.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ChatLibConfig {
	private ChatLib plugin;
	private FileConfiguration config;
	private File saveFile;
	
	public boolean isPluginEnabled = true;
	public boolean isDebugging = false;
	public String lang = "eng";
	
	public String serverName = "yourServer";
	
	public int hoverMaxLines = 20;
	public int hoverMaxLength = 50;
	
	public boolean enableChatModification = true;
	
	ChatLibConfig(Plugin plugin){
		this.plugin = (ChatLib) plugin;

		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		
		saveFile = new File(plugin.getDataFolder(), "config.yml");
		if(!saveFile.exists())
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		config = YamlConfiguration.loadConfiguration(saveFile);
		
		load();
		save();
	}
	
	public void load(){
		this.isPluginEnabled = config.getBoolean("Plugin.Enable", true);
		this.isDebugging = config.getBoolean("Plugin.Debug", false);
		this.lang = config.getString("Plugin.Lang", "eng");
		
		this.serverName = config.getString("Plugin.serverName", "yourServer");
		
		this.hoverMaxLines = config.getInt("hoverMaxLines", 20);
		this.hoverMaxLength = config.getInt("hoverMaxLength", 50);
		
		this.enableChatModification = config.getBoolean("enableChatModification", true);
	}
	
	public void save(){
		config.set("Plugin.Enable", Boolean.valueOf(isPluginEnabled));
		config.set("Plugin.Debug", Boolean.valueOf(isDebugging));
		config.set("Plugin.Lang", lang);
		
		config.set("Plugin.serverName", serverName);
		
		config.set("hoverMaxLines", hoverMaxLines);
		config.set("hoverMaxLength", hoverMaxLength);
		
		config.set("enableChatModification", enableChatModification);
		try {
			config.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload(){
		try {
			config.load(saveFile);
			config.save(saveFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
