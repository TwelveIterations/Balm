package net.blay09.mods.balm.neoforge.platform.resources.internal;

import net.blay09.mods.balm.platform.resources.ModResource;
import net.neoforged.fml.jarcontents.JarResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public record NeoForgeModResource(String path, JarResource resource) implements ModResource {
    @Override
    public BufferedReader bufferedReader(Charset charset) throws IOException {
        return resource.bufferedReader(charset);
    }

    @Override
    public InputStream inputStream() throws IOException {
        return resource.open();
    }

    @Override
    public byte[] bytes() throws IOException {
        return resource.readAllBytes();
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String name() {
        final var lastSepIndex = path().lastIndexOf(File.pathSeparatorChar);
        if (lastSepIndex == -1) {
            return path;
        }
        return path.substring(lastSepIndex + 1);
    }
}
