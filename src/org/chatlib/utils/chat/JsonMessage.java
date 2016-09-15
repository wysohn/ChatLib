package org.chatlib.utils.chat;

import org.chatlib.utils.chat.handlers.JsonMessageClickEvent;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class JsonMessage {
	private boolean bold = false;
	private boolean italic = false;
	private boolean underlined = false;
	private boolean strikethrough = false;
	private boolean obfuscated = false;

	private String color = "reset";

	private JsonMessageClickEvent clickEvent = null;
	private JsonMessageHoverEvent hoverEvent = null;

	public boolean isBold() {
		return bold;
	}

	public JsonMessage setBold(boolean bold) {
		this.bold = bold;
		return this;
	}

	public boolean isItalic() {
		return italic;
	}

	public JsonMessage setItalic(boolean italic) {
		this.italic = italic;
		return this;
	}

	public boolean isUnderlined() {
		return underlined;
	}

	public JsonMessage setUnderlined(boolean underlined) {
		this.underlined = underlined;
		return this;
	}

	public boolean isStrikethrough() {
		return strikethrough;
	}

	public JsonMessage setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
		return this;
	}

	public boolean isObfuscated() {
		return obfuscated;
	}

	public JsonMessage setObfuscated(boolean obfuscated) {
		this.obfuscated = obfuscated;
		return this;
	}

	public String getColor() {
		return color;
	}

	/**
    black,
    dark_blue,
    dark_green,
    dark_aqua,
    dark_red,
    dark_purple,
    gold,
    gray,
    dark_gray,
    blue,
    green,
    aqua,
    red,
    light_purple,
    yellow,
    white,
    obfuscated,
    bold,
    strikethrough,
    underline,
    italic,
    reset
     * 
     * @param color
     */
	public JsonMessage setColor(String color) {
		this.color = color;
		return this;
	}

	public JsonMessageClickEvent getClickEvent() {
		return clickEvent;
	}

	public JsonMessage setClickEvent(JsonMessageClickEvent clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}

	public JsonMessageHoverEvent getHoverEvent() {
		return hoverEvent;
	}

	public JsonMessage setHoverEvent(JsonMessageHoverEvent hoverEvent) {
		this.hoverEvent = hoverEvent;
		return this;
	}

	public String buildJsonString(Gson gson) {
		return gson.toJson(this);
	}
	
	public JsonElement buildJsonElement(Gson gson){
		return gson.toJsonTree(this);
	}
}
