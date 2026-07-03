package net.blay09.mods.balm.neoforge.platform.compatibility.internal;

import net.blay09.mods.balm.neoforge.platform.compatibility.milk.internal.NeoForgeBalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.hudinfo.internal.CommonBalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.multiminers.BalmModSupportMultiMiners;
import net.blay09.mods.balm.platform.compatibility.multiminers.internal.MultiMinersMultiplexer;
import net.blay09.mods.balm.platform.compatibility.multiminers.internal.NoopMultiMiners;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.CommonBalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.NoopTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.TrinketsMultiplexer;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class NeoForgeBalmModSupport implements BalmModSupport {
    private final Supplier<BalmModSupportMultiMiners> multiminers;
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();
    private final CommonBalmModSupportRecipeViewer recipeViewers = new CommonBalmModSupportRecipeViewer();
    private final BalmModSupportMilkFluid milkFluid = new NeoForgeBalmModSupportMilkFluid();

    public NeoForgeBalmModSupport(BalmRuntime<?> runtime) {
        multiminers = runtime.<BalmModSupportMultiMiners>modProxy(Identifier.fromNamespaceAndPath("balm", "multiminers"))
                .with("ftbultimine", "net.blay09.mods.balm.platform.compatibility.multiminers.internal.FTBUltimineIntegration")
                .withMultiplexer(MultiMinersMultiplexer::new)
                .withFallback(new NoopMultiMiners())
                .buildLazily();
        trinkets = runtime.<BalmModSupportTrinkets>modProxy(Identifier.fromNamespaceAndPath("balm", "trinkets"))
                .with("curios", "net.blay09.mods.balm.neoforge.platform.compatibility.trinkets.internal.CuriosIntegration")
                .with("trinkets_updated", "net.blay09.mods.balm.platform.compatibility.trinkets.internal.TrinketsUpdatedIntegration")
                .withMultiplexer(TrinketsMultiplexer::new)
                .withFallback(new NoopTrinkets())
                .buildLazily();
    }

    @Override
    public BalmModSupportMultiMiners multiminers() {
        return multiminers.get();
    }

    @Override
    public BalmModSupportTrinkets trinkets() {
        return trinkets.get();
    }

    @Override
    public BalmModSupportHudInfo hudInfo() {
        return hudInfo;
    }

    @Override
    public BalmModSupportMilkFluid milkFluid() {
        return milkFluid;
    }

    @Override
    public BalmModSupportRecipeViewer recipeViewers() {
        return recipeViewers;
    }
}
