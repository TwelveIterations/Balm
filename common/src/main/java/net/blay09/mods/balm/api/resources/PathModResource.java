package net.blay09.mods.balm.api.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public record PathModResource(Path path) implements ModResource {
    @Override
    public BufferedReader bufferedReader(Charset charset) throws IOException {
        return Files.newBufferedReader(path, charset);
    }

    @Override
    public InputStream inputStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public byte[] bytes() throws IOException {
        return Files.readAllBytes(path);
    }
}
