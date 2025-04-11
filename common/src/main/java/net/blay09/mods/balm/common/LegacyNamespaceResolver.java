package net.blay09.mods.balm.common;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public record LegacyNamespaceResolver(Supplier<String> defaultProvider) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return defaultProvider.get();
    }

    @Override
    public String getNamespaceFor(ResourceLocation identifier) {
        return identifier.getNamespace();
    }
}