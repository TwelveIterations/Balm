package net.blay09.mods.balm.platform.resources.internal;

import net.blay09.mods.balm.platform.resources.ModResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public record PathModResource(Path nioPath) implements ModResource {
    @Override
    public BufferedReader bufferedReader(Charset charset) throws IOException {
        return Files.newBufferedReader(nioPath, charset);
    }

    @Override
    public InputStream inputStream() throws IOException {
        return Files.newInputStream(nioPath);
    }

    @Override
    public byte[] bytes() throws IOException {
        return Files.readAllBytes(nioPath);
    }

    @Override
    public String path() {
        return nioPath.toString();
    }

    @Override
    public String name() {
        return nioPath.getFileName().toString();
    }

}
