package net.blay09.mods.balm.common;

import java.util.function.Supplier;

/**
 * @deprecated To be removed with the registries refactor.
 */
@Deprecated
public record LegacyNamespaceResolver(Supplier<String> defaultProvider) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return defaultProvider.get();
    }
}