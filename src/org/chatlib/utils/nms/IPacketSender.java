package org.chatlib.utils.nms;

import org.bukkit.entity.Player;
import org.chatlib.main.ChatLib;

import me.clip.placeholderapi.PlaceholderAPI;

public abstract class IPacketSender {
	final public void sendPlayOutChat(Player player, String json){
		if(player.isConversing())
			return;
		
		if(ChatLib.isPlaceHolderAPIEnabled()){
			json = PlaceholderAPI.setPlaceholders(player, json);
		}
		
		sendPlayOutChat(player, json, true);
	}
	protected abstract void sendPlayOutChat(Player player, String json, boolean dummy);
}
