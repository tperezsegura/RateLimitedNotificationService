package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static final String RATE_LIMIT_RULES_FILE_PATH = "src/main/resources/rate_limit_rules.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFile(TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(new File(RATE_LIMIT_RULES_FILE_PATH), typeReference);
    }

}
