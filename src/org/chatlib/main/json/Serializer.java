package org.chatlib.main.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface Serializer<T> extends JsonSerializer<T>, JsonDeserializer<T>{

}
