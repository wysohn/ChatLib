package org.chatlib.main;

import java.util.Map;

import org.bukkit.entity.Player;
import org.chatlib.constants.ChatUser;
import org.chatlib.utils.chat.JsonMessage;

public class ChatLibAPI {
	public static Map<String, Object> getInfo(Player player){
		ChatUser user = ChatLib.getChatUserManager().getChatUser(player);
		if(user == null) return null;
		
		return user.getInfos();
	}
	
	public static Map<String, JsonMessage[]> getPrefixes(Player player){
		ChatUser user = ChatLib.getChatUserManager().getChatUser(player);
		if(user == null) return null;
		
		return user.getPrefixes();
	}
	
	public static Map<String, JsonMessage[]> getSuffixes(Player player){
		ChatUser user = ChatLib.getChatUserManager().getChatUser(player);
		if(user == null) return null;
		
		return user.getSuffixes();
	}
	
	public static void sendJsonMessage(Player player, String json){
		ChatLib.getPacketSender().sendPlayOutChat(player, json);
	}
	
	public static String toJsonString(JsonMessage[] message){
		Object[] objs = new Object[message.length + 1];
		for(int i = 1; i < message.length + 1; i++)
			objs[i] = message[i - 1];
		objs[0] = "";
		return ChatLib.getGson().toJson(objs);
	}
	
	public static String toJsonString(JsonMessage message){
		return message.buildJsonString(ChatLib.getGson());
	}
}
