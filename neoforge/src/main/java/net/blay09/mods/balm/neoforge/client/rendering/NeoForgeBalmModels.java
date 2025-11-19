package net.blay09.mods.balm.neoforge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public record NeoForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels {

    public static class AdditionalModel implements ModelDebugName {
        private final Identifier identifier;
        private final StandaloneModelKey<BlockStateModel> modelKey = new StandaloneModelKey<>(this);

        public AdditionalModel(Identifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public String debugName() {
            return identifier.toString();
        }

        public Identifier identifier() {
            return identifier;
        }

        public StandaloneModelKey<BlockStateModel> modelKey() {
            return modelKey;
        }
    }

    @Override
    public DeferredObject<BlockStateModel> loadModel(Identifier identifier) {
        final var registrations = getActiveRegistrations();
        final var additionalModel = new AdditionalModel(identifier);
        final var deferredModel = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                return Minecraft.getInstance().getModelManager().getStandaloneModel(additionalModel.modelKey());
            }

            @Override
            public boolean canResolve() {
                final var model = Minecraft.getInstance().getModelManager().getStandaloneModel(additionalModel.modelKey());
                return model != null;
            }
        };
        registrations.additionalModels.add(additionalModel);
        return deferredModel;
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    @Override
    public BalmModels scoped(String modId) {
        return new NeoForgeBalmModels(new StaticNamespaceResolver(modId));
    }

    public static class Registrations {
        public final List<AdditionalModel> additionalModels = new ArrayList<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterStandalone event) {
            for (final var entry : additionalModels) {
                event.register(entry.modelKey(), SimpleUnbakedStandaloneModel.blockStateModel(entry.identifier()));
            }
        }
    }

}
