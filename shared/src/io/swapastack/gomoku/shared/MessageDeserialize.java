package io.swapastack.gomoku.shared;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageDeserialize implements JsonDeserializer<ExtractorMessage> {

    @Override
    public ExtractorMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Gson gson = new Gson();
        switch (object.get("messageType").getAsString()) {
            case ("GoodbyeClient"):
                return gson.fromJson(json, (Type) GoodbyeClient.class);

            case ("GoodbyeServer"):
                return gson.fromJson(json, (Type) GoodbyeServer.class);

            case ("HelloServer"):
                return gson.fromJson(json, (Type) HelloServer.class);

            case ("HistoryAll"):
                return gson.fromJson(json, (Type) HistoryAll.class);

            case ("HistoryGetAll"):
                return gson.fromJson(json, (Type) HistoryGetAll.class);

            case ("HistoryNotSaved"):
                return gson.fromJson(json, (Type) HistoryNotSaved.class);

            case ("HistoryPush"):
                return gson.fromJson(json, (Type) HistoryPush.class);

            case ("HistorySaved"):
                return gson.fromJson(json, (Type) HistorySaved.class);

            case ("WelcomeClient"):
                return gson.fromJson(json, (Type) WelcomeClient.class);

        }
        return null;
    }
}
