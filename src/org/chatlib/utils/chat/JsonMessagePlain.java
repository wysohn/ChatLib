package org.chatlib.utils.chat;

public class JsonMessagePlain extends JsonMessage {
	private String text;

	public JsonMessagePlain(String text) {
		this.text = text;
	}

	public JsonMessagePlain setText(String text) {
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}
	
}
