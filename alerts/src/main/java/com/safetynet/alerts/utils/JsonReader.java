package com.safetynet.alerts.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    private static final String FILE_PATH = "src/main/resources/data.json";

    public static <T> List<T> loadData(Class<T> valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(FILE_PATH), objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
    }
}
