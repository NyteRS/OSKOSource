package mgi.tools.parser;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.lang.reflect.Type;
import java.util.Map;

public class Int2ObjectMapDeserializer implements JsonDeserializer<Int2ObjectMap<Object>> {
    @Override
    public Int2ObjectMap<Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Int2ObjectMap<Object> map = new Int2ObjectOpenHashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            int key = Integer.parseInt(entry.getKey());
            Object value = context.deserialize(entry.getValue(), Object.class);
            map.put(key, value);
        }
        return map;
    }
}

