package com.pagonxt.onetradefinance.integrations.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    private static final TestUtils INSTANCE = new TestUtils();

    private TestUtils() {
    }

    public static String getRawFile(String fileName) {
        return INSTANCE.readFile(fileName);
    }

    private String readFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            assert resource != null;
            return Files.readString(Paths.get(resource.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Error getting template '%s'", fileName), e);
        }
    }
}
