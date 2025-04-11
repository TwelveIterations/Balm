package net.blay09.mods.balm.common;

import net.minecraft.resources.ResourceLocation;

public record StaticNamespaceResolver(String modId) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return modId;
    }

    @Override
    public String getNamespaceFor(ResourceLocation identifier) {
        return modId;
    }
}