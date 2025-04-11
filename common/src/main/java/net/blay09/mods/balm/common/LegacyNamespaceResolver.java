package net.blay09.mods.balm.common;

import java.util.function.Supplier;

public record LegacyNamespaceResolver(Supplier<String> defaultProvider) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return defaultProvider.get();
    }
}