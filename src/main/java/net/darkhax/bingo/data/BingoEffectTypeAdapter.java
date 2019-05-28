package net.darkhax.bingo.data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bingo.BingoMod;

public class BingoEffectTypeAdapter<T> implements JsonDeserializer<T> {

    private final Map<String, Class<? extends T>> effectClasses = new HashMap<>();

    public void registerEffect (String key, Class<? extends T> effectClass) {

        if (this.effectClasses.containsKey(key)) {
            
            BingoMod.LOG.warn("Could not register {} with id {}. It is already assigned to {}.", effectClass.getName(), key, this.effectClasses.get(key).getName());
        }
        
        else {
            
            this.effectClasses.put(key, effectClass);
        }
    }

    @Override
    public T deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        // Get the json data as an explorable object.
        final JsonObject jsonObject = json.getAsJsonObject();

        // Get the value of the type field.
        final JsonElement jsonType = jsonObject.get("type");

        // Fail early if there is no type value defined by the entry.
        if (jsonType == null) {

            BingoMod.LOG.error("Could not load an effect, it has no type value. Data: {}", jsonObject.toString());
            return null;
        }

        // Read the string from the json element.
        final String type = jsonType.getAsString();

        // Lookup the class from the deserialization map.
        final Class<? extends T> effectClass = effectClasses.get(type);

        // Fail early if the class does not exist.
        if (effectClass == null) {

            BingoMod.LOG.error("Could not find a type for {}", type);
            return null;
        }

        // Deserialize the json data to the specified type.
        return context.deserialize(jsonObject, effectClass);
    }
}