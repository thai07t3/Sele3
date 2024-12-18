package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Localization {
    private final JsonNode languageData;

    public Localization(String language) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(Constants.LANGUAGE_PATH + language + ".json");
            languageData = mapper.readTree(file);
            if (languageData == null) {
                throw new IllegalArgumentException("Language file not found or empty: " + language);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load localization file for language: " + language, e);
        }
    }

    public String getLocator(String key) {
        return languageData.get("locator").get(key).asText();
    }

    public String getContent(String key) {
        return languageData.get("content").get(key).asText();
    }
}
