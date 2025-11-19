package net.blay09.mods.balm.platform.compatibility.hudinfo;

@FunctionalInterface
public interface BlockInfoProvider {
    void apply(BlockInfoContext context, HudInfoOutput output);
}
