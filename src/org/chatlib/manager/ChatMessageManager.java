package org.chatlib.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.chatlib.constants.ChatUser;
import org.chatlib.main.ChatLib;
import org.chatlib.main.ChatLibAPI;
import org.chatlib.main.LanguageSupport.Languages;
import org.chatlib.utils.chat.JsonMessage;
import org.chatlib.utils.chat.JsonMessagePlain;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent.ClickAction;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent.HoverAction;

public class ChatMessageManager extends Manager implements Listener{

	public ChatMessageManager(Plugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e){
		if(!ChatLib.config.enableChatModification)
			return;
		
		if(e.isCancelled()) return;
		
		e.setCancelled(true);
		
		ChatUser user = ChatLib.getChatUserManager().getChatUser(e.getPlayer());
		
		JsonMessagePlain playerInfo = new JsonMessagePlain(e.getPlayer().getName());
		playerInfo.setClickEvent(new JsonMessageClickEvent(ClickAction.suggest_command, "/w "+e.getPlayer().getName()+" "));
		playerInfo.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, user.toString()));
		
		String[] groups = ChatLib.chat.getPlayerGroups(e.getPlayer());
		String prefix = ChatLib.chat.getPlayerPrefix(e.getPlayer());
		String suffix = ChatLib.chat.getPlayerSuffix(e.getPlayer());
		if(groups.length != 0){
			if(prefix == null || prefix.length() == 0) prefix = ChatLib.chat.getGroupPrefix(e.getPlayer().getWorld(), groups[0]);
			if(suffix == null || prefix.length() == 0) suffix = ChatLib.chat.getGroupSuffix(e.getPlayer().getWorld(), groups[0]);
		}
		if(prefix != null) playerInfo.setText(ChatColor.translateAlternateColorCodes('&', prefix) + playerInfo.getText());
		if(suffix != null) playerInfo.setText(playerInfo.getText() + ChatColor.translateAlternateColorCodes('&', suffix));
		playerInfo.setText(playerInfo.getText());
		
		String format = e.getFormat();
		String[] split = format.split("%\\d\\$s");
		
		List<JsonMessage> list = new ArrayList<JsonMessage>();
		//build prefixes first
		for(JsonMessage[] prefixes : user.getPrefixes().values()){
			for(JsonMessage msg : prefixes) list.add(msg);
		}
		list.add(new JsonMessagePlain(split[0]));
		list.add(playerInfo);
		for(JsonMessage[] suffixes : user.getSuffixes().values()){
			for(JsonMessage msg : suffixes) list.add(msg);
		}
		list.add(new JsonMessagePlain(split[1]));
		for(String str : e.getMessage().split(" ")){
			if(str.contains("http://") || str.contains("https://")){
				JsonMessagePlain msg = new JsonMessagePlain(str+" ");
				msg.setClickEvent(new JsonMessageClickEvent(ClickAction.open_url, str));
				msg.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, 
						ChatLib.getLang().parseFirstString(Languages.HoverMesssage_Url_Warning)));
				list.add(msg);
			}else{
				list.add(new JsonMessagePlain(str+" "));
			}
		}
		String json = ChatLibAPI.toJsonString(list.toArray(new JsonMessagePlain[list.size()]));
		for(Player player : e.getRecipients()){
			ChatLib.getPacketSender().sendPlayOutChat(player, json);
		}
		Bukkit.getConsoleSender().sendMessage(e.getPlayer().getName()+": "+e.getMessage());
	}
	
	@Override
	public void onDisable() {
		
	}

}
