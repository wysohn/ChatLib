package org.chatlib.utils.chat;

import com.google.gson.JsonObject;

public class JsonMessageScoreboard extends JsonMessage{
	private final JsonObject score;

	public JsonMessageScoreboard(String playerName, String objectivename) {
		JsonObject json = new JsonObject();
		json.addProperty("name", playerName);
		json.addProperty("objective", objectivename);
		score = json;
	}

	public JsonObject getScore() {
		return score;
	}
	
}
