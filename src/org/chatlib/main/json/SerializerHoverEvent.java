package org.chatlib.main.json;

import java.lang.reflect.Type;

import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent.HoverAction;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class SerializerHoverEvent implements Serializer<JsonMessageHoverEvent> {

	@Override
	public JsonElement serialize(JsonMessageHoverEvent arg0, Type arg1, JsonSerializationContext arg2) {
		JsonObject json = new JsonObject();
		json.add("action", arg2.serialize(arg0.getAction()));
		json.addProperty("value", arg0.getValue());
		return json;
	}

	@Override
	public JsonMessageHoverEvent deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject json = (JsonObject) arg0;
		HoverAction action = arg2.deserialize(json.get("action"), HoverAction.class);
		String value = json.get("value").getAsString();
		
		return new JsonMessageHoverEvent(action, value.split("\n"));
	}

}
