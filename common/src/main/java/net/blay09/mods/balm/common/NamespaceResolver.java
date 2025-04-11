package net.blay09.mods.balm.common;

import net.minecraft.resources.ResourceLocation;

public interface NamespaceResolver {
    String getDefaultNamespace();

    String getNamespaceFor(ResourceLocation identifier);

    default String getMatchingNamespaceOrThrow(ResourceLocation identifier) {
        final var effectiveNamespace = getNamespaceFor(identifier);
        if (!effectiveNamespace.equals(identifier.getNamespace())) {
            throw new IllegalArgumentException("Identifier " + identifier + " has an invalid namespace. Expected " + effectiveNamespace + " but got " + identifier.getNamespace());
        }
        return effectiveNamespace;
    }
}
