package net.blay09.mods.balm.common;

public record StaticNamespaceResolver(String modId) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return modId;
    }
}