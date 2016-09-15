package org.chatlib.main;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.chatlib.main.LanguageSupport.Languages;
import org.chatlib.main.json.SerializerClickEvent;
import org.chatlib.main.json.SerializerHoverEvent;
import org.chatlib.manager.ChatMessageManager;
import org.chatlib.manager.ChatUserManager;
import org.chatlib.manager.Manager;
import org.chatlib.manager.api.JobsAPISupport;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent;
import org.chatlib.utils.nms.IPacketSender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.clip.placeholderapi.PlaceholderHook;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class ChatLib extends JavaPlugin{
	private static String PLUGIN_NAME;
	
	private String version;
	private String packageName = "org.chatlib.utils.nms";
	
	private static GsonBuilder gsonBuilder;
	
	static{
		gsonBuilder = new GsonBuilder();
		
		gsonBuilder.registerTypeAdapter(JsonMessageClickEvent.class, new SerializerClickEvent());
		gsonBuilder.registerTypeAdapter(JsonMessageHoverEvent.class, new SerializerHoverEvent());
	}
	
	private static IPacketSender packetSender;
	
	private static JobsAPISupport jobsAPI;
	
	public static ChatLibConfig config;
	private static LanguageSupport lang;
	private static CommandExecutor cmdExe;
	
	public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;
    
    private static boolean isPlaceHolderAPIEnabled = false;
    private static boolean isJobsEnabled = false;
	
	private static ChatMessageManager chatManager;
	private static ChatUserManager userManager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		cmdExe.onCommand(sender, command, label, args);
		
		return true;
	}

	@Override
	public void onEnable() {
		PLUGIN_NAME = this.getDescription().getFullName();
		
		config = new ChatLibConfig(this);
		try {
			lang = new LanguageSupport(this, config.lang);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		cmdExe = new CommandExecutor(this);
		
		if(!config.isPluginEnabled){
			this.setEnabled(false);
			ChatLib.logInfo("["+PLUGIN_NAME+"] "+lang.parseFirstString(Languages.Plugin_NotEnabled));
			ChatLib.logInfo(lang.parseFirstString(Languages.Plugin_SetEnableToTrue));
			ChatLib.logInfo(lang.parseFirstString(Languages.Plugin_WillBeDisabled));
			return;
		}
		
		PluginManager pm = getServer().getPluginManager();
		//init goes here
		userManager = new ChatUserManager(this);
		
		chatManager = new ChatMessageManager(this);	
		
		if(!setupPermissions()){
			getLang().addString("Permission");
			logInfo(getLang().parseFirstString(Languages.Plugin_VersionNotSupported));
			this.setEnabled(false);
		}
		if(!setupChat()){
			getLang().addString("Chat");
			logInfo(getLang().parseFirstString(Languages.Plugin_VersionNotSupported));
			this.setEnabled(false);
		}
		if(!setupEconomy()){
			getLang().addString("Economy");
			logInfo(getLang().parseFirstString(Languages.Plugin_VersionNotSupported));
			this.setEnabled(false);
		}
		
		Plugin ph = pm.getPlugin("PlaceholderAPI");
		if(ph != null && ph.isEnabled()){
			isPlaceHolderAPIEnabled = true;
			logInfo("PlaceholderAPI found! "+ph.getDescription().getVersion());
			logInfo("Enabling support for PlaceholderAPI");
		}
		
		Plugin jobs = pm.getPlugin("Jobs");
		if(jobs != null && jobs.isEnabled()){
			isJobsEnabled = true;
			
			jobsAPI = new JobsAPISupport(this);
			pm.registerEvents(jobsAPI, this);
			
			logInfo("Jobs found! "+jobs.getDescription().getVersion());
			logInfo("Enabling support for Jobs");
		}
		
		//nms
		String packageName = getServer().getClass().getPackage().getName();
		version = packageName.substring(packageName.lastIndexOf('.') + 1);
		
		initNMS();
		//init ends
		for(Manager manager : Manager.getModules()){
			if(manager == null){
				logDebug("Null manager found. skipped.");
				continue;
			}
			
			try {
				pm.registerEvents((Listener) manager, this);
			} catch (Exception e) {
				logDebug("Exception : "+manager.getClass().getSimpleName());
			} catch (Error e){
				logDebug("Error : "+manager.getClass().getSimpleName());
			}
		}

		super.onEnable();
	}
	
	private void initNMS(){
		try {
			Class<?> clazz = Class.forName(packageName+ "." + version + "." + "PacketSender");
			packetSender = (IPacketSender) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			this.setEnabled(false);
			getLang().addString(version);
			logInfo(getLang().parseFirstString(Languages.Plugin_VersionNotSupported));
			logInfo(getLang().parseFirstString(Languages.Plugin_WillBeDisabled));
		} catch (InstantiationException e) {
			this.setEnabled(false);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			this.setEnabled(false);
			e.printStackTrace();
		}
		
	}

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

	public static void logDebug(String str){
		if(!config.isDebugging)
			return;
		
		Bukkit.getLogger().warning("["+PLUGIN_NAME+"(Debug)] "+str);
	}
	
	public static void logInfo(String str){
		Bukkit.getLogger().info("["+PLUGIN_NAME+"] "+str);
	}
	
	public static void invokeJS(String js){
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		try {
			engine.eval(js);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isPlaceHolderAPIEnabled() {
		return isPlaceHolderAPIEnabled;
	}

	public static boolean isJobsEnabled() {
		return isJobsEnabled;
	}

	public static Gson getGson(){
		return gsonBuilder.create();
	}

	public static IPacketSender getPacketSender() {
		return packetSender;
	}

	public static LanguageSupport getLang() {
		return lang;
	}

	public static CommandExecutor getCmdExe() {
		return cmdExe;
	}

	public static ChatMessageManager getChatMessageManager() {
		return chatManager;
	}

	public static ChatUserManager getChatUserManager() {
		return userManager;
	}

	public static JobsAPISupport getJobsAPI() {
		return jobsAPI;
	}
	
}
