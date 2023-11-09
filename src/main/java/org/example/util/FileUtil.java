package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.RateLimitConfig;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readRateLimitRulesFile(TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(new File(RateLimitConfig.getRateLimitRulesFilePath()), typeReference);
    }

}