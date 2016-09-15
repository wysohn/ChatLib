package org.chatlib.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.chatlib.constants.ChatUser;
import org.chatlib.main.ChatLib;
import org.chatlib.main.LanguageSupport.Languages;

public class ChatUserManager extends Manager implements Listener{
	private static Map<UUID, ChatUser> userList = new ConcurrentHashMap<UUID, ChatUser>();
	
	public ChatUserManager(Plugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e){
		ChatUser user = new ChatUser(e.getPlayer());
		userList.put(e.getPlayer().getUniqueId(), user);
		
		ChatLib.getLang().addString(e.getPlayer().getName());
		user.getInfos().put("name", ChatLib.getLang().parseFirstString(Languages.HoverMesssage_Name));
		
		String[] groups = ChatLib.permission.getPlayerGroups(e.getPlayer());
		ChatLib.getLang().addString(groups.length != 0 ? groups[0] : "None");
		user.getInfos().put("group", ChatLib.getLang().parseFirstString(Languages.HoverMesssage_Group));
		
		final Player player = e.getPlayer();
		if(ChatLib.isJobsEnabled()){
			Bukkit.getScheduler().runTask(plugin, new Runnable(){
				@Override
				public void run() {
					if(ChatLib.isJobsEnabled()){
						ChatLib.getJobsAPI().putJobsInfo(player);
					}
				}
			});
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		userList.remove(e.getPlayer().getUniqueId());
	}
	

	
	public ChatUser getChatUser(Player p){
		if(p == null)
			return null;
		return userList.get(p.getUniqueId());
	}
	
	public ChatUser getChatUser(UUID uuid){
		return userList.get(uuid);
	}
	
	@Override
	public void onDisable() {
		
	}

}
