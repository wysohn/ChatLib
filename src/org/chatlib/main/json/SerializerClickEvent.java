package org.chatlib.main.json;

import java.lang.reflect.Type;

import org.chatlib.utils.chat.handlers.JsonMessageClickEvent;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent.ClickAction;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class SerializerClickEvent implements Serializer<JsonMessageClickEvent> {

	@Override
	public JsonElement serialize(JsonMessageClickEvent arg0, Type arg1, JsonSerializationContext arg2) {
		JsonObject json = new JsonObject();
		json.add("action", arg2.serialize(arg0.getAction()));
		json.addProperty("value", arg0.getValue());
		return json;
	}

	@Override
	public JsonMessageClickEvent deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject json = (JsonObject) arg0;
		ClickAction action = arg2.deserialize(json.get("action"), ClickAction.class);
		String value = json.get("value").getAsString();
		
		return new JsonMessageClickEvent(action, value);
	}

}
