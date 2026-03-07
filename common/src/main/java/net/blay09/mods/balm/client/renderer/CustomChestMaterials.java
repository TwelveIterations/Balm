package net.blay09.mods.balm.client.renderer;

import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomChestMaterials {

    @FunctionalInterface
    public interface MaterialProvider {
        SpriteId getMaterial(ChestRenderState chestRenderState);
    }

    private static final Map<BlockEntityType<?>, MaterialProvider> materialProviders = new ConcurrentHashMap<>();

    public static <T extends BlockEntity> void register(BlockEntityType<T> type, SpriteId material) {
        register(type, (chestRenderState) -> material);
    }

    public static <T extends BlockEntity> void register(BlockEntityType<T> type, MaterialProvider materialFunction) {
        materialProviders.put(type, materialFunction);
    }

    @Nullable
    public static SpriteId getMaterial(ChestRenderState chestRenderState) {
        final var provider = materialProviders.get(chestRenderState.blockEntityType);
        if (provider != null) {
            return provider.getMaterial(chestRenderState);
        }
        return null;
    }

}
