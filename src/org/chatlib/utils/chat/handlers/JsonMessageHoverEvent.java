package org.chatlib.utils.chat.handlers;

import org.bukkit.ChatColor;
import org.chatlib.main.ChatLib;

public class JsonMessageHoverEvent {
	private final HoverAction action;
	private final String value;
	
	public JsonMessageHoverEvent(HoverAction action, String value) {
		this.action = action;
		
/*		int lines = 0;
		String combined = "";
		String[] sentences = value.split(" ");
		int length = 0;
		for(String sentence : sentences){
			if(lines > MAXLINES)
				break;
			
			length += sentence.length();
			if(length > MAXLENGTH){
				length = 0;
				
				combined += "\n"+ChatColor.getLastColors(combined);
				lines++;
				combined += sentence + " ";
			}else{
				combined += sentence + " ";
			}
		}
		
		if(lines >= MAXLINES){
			combined += "\n...";
		}
		this.value = combined;*/
		
		this.value = value;
	}
	
	private static final int MAXLINES = ChatLib.config.hoverMaxLines;
	private static final int MAXLENGTH = ChatLib.config.hoverMaxLength;
	public JsonMessageHoverEvent(HoverAction action, String[] value) {
		this.action = action;
		
		int lines = 0;
		String combined = "";
		for(int i=0;i<value.length && lines < MAXLINES;i++){
			String[] sentences = value[i].split(" ");
			int length = 0;
			for(String sentence : sentences){
				length += sentence.length();
				if(length > MAXLENGTH){
					length = 0;
					
					combined += "\n"+ChatColor.getLastColors(combined);
					lines++;
					combined += sentence + " ";
				}else{
					combined += sentence + " ";
				}
			}
			
			if(i != value.length - 1) {
				combined += "\n";
				lines++;
			}
		}
		if(lines >= MAXLINES){
			combined += "\n...";
		}
		this.value = combined;
	}

	public HoverAction getAction() {
		return action;
	}

	public String getValue() {
		return value;
	}

	public static enum HoverAction{
		show_text,
		show_achievement,
		show_item,
		
	}
}
