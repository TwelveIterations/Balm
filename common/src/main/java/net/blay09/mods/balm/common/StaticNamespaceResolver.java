package net.blay09.mods.balm.common;

/**
 * @deprecated To be removed with the registries refactor.
 */
@Deprecated
public record StaticNamespaceResolver(String modId) implements NamespaceResolver {
    @Override
    public String getDefaultNamespace() {
        return modId;
    }
}