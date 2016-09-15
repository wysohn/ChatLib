package org.chatlib.utils.chat.handlers;

public class JsonMessageClickEvent {
	private final ClickAction action;
	private final String value;
	
	public JsonMessageClickEvent(ClickAction action, String value) {
		this.action = action;
		this.value = value;
	}

	public ClickAction getAction() {
		return action;
	}

	public String getValue() {
		return value;
	}
	
	public static enum ClickAction{
		open_url,
		open_file,
		run_command,
		suggest_command;
	}
}
