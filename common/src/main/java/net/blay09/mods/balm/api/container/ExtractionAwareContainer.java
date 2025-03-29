package net.blay09.mods.balm.api.container;

/**
 * @deprecated This interface is only respected when extracting through Balm's ContainerUtils. Use Container.canTakeItem or WorldlyContainer instead.
 */
@Deprecated(forRemoval = true, since = "1.21.5")
public interface ExtractionAwareContainer {
    boolean canExtractItem(int slot);
}