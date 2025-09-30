package net.blay09.mods.balm.neoforge.resources;

import cpw.mods.jarhandling.JarResource;
import net.blay09.mods.balm.api.resources.ModResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public record NeoForgeModResource(JarResource resource) implements ModResource {
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
}
