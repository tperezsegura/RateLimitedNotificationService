package org.example.config;

public class RateLimitConfig {

    private static final String PRODUCTION_RATE_LIMIT_RULES_FILE_PATH = "src/main/resources/rate_limit_rules.json";
    private static final String TEST_RATE_LIMIT_RULES_FILE_PATH = "src/test/resources/test_rate_limit_rules.json";

    public static String getRateLimitRulesFilePath() {
        String environment = System.getProperty("env", "production");
        return environment.equals("test") ? TEST_RATE_LIMIT_RULES_FILE_PATH : PRODUCTION_RATE_LIMIT_RULES_FILE_PATH;
    }

}