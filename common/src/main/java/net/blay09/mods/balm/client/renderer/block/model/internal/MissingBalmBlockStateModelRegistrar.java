package net.blay09.mods.balm.client.renderer.block.model.internal;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class MissingBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar implements DeferredBlockStateModel {
    private static final List<Consumer<BalmBlockStateModelRegistrar>> initializers = new CopyOnWriteArrayList<>();
    private static boolean bootstrapped;

    public static synchronized void bootstrap(Consumer<BalmBlockStateModelRegistrar> initializer) {
        initializers.add(initializer);
        if (!bootstrapped) {
            BalmClient.getRuntime().resourceReloadListeners("balm", listeners -> listeners.register("block_state_models_fallback",
                    (_, preparationExecutor, barrier, _) -> CompletableFuture.runAsync(() -> {
                        final var registrar = new MissingBalmBlockStateModelRegistrar();
                        initializers.forEach(callback -> callback.accept(registrar));
                    }, preparationExecutor).thenCompose(barrier::wait)));
            bootstrapped = true;
        }
    }

    @Override
    public DeferredBlockStateModel register(Identifier identifier) {
        return this;
    }

    @Override
    public BlockStateModel asBlockStateModel() {
        return Minecraft.getInstance().getModelManager().getBlockStateModelSet().missingModel();
    }
}
