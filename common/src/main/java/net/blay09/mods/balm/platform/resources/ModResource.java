package net.blay09.mods.balm.platform.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface ModResource {
    default BufferedReader bufferedReader() throws IOException {
        return bufferedReader(StandardCharsets.UTF_8);
    }

    BufferedReader bufferedReader(Charset charset) throws IOException;

    InputStream inputStream() throws IOException;

    byte[] bytes() throws IOException;

    String path();

    String name();

    default String extension() {
        final var lastDotIndex = path().lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }

        return path().substring(lastDotIndex + 1);
    }
}
