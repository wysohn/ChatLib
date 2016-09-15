package org.chatlib.constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.chatlib.main.ChatLib;
import org.chatlib.main.LanguageSupport.Languages;
import org.chatlib.utils.chat.JsonMessage;

public class ChatUser {
	private final Player player;
	private final Map<String, Object> infos = new LinkedHashMap<String, Object>();
	private final Map<String, JsonMessage[]> prefixes = new HashMap<String, JsonMessage[]>();
	private final Map<String, JsonMessage[]> suffixes = new HashMap<String, JsonMessage[]>();
	
	public ChatUser(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public Map<String, Object> getInfos() {
		return infos;
	}
	
	public Map<String, JsonMessage[]> getPrefixes() {
		return prefixes;
	}

	public Map<String, JsonMessage[]> getSuffixes() {
		return suffixes;
	}

	@Override
	public String toString() {
		String info = "";
		for (Object obj : infos.values())
			info += obj.toString() + "\n";
		return info + "\n" + ChatLib.getLang().parseFirstString(Languages.HoverMesssage_ClickToWhisper);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatUser other = (ChatUser) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.getName().equals(other.player.getName()))
			return false;
		return true;
	}
	
}
