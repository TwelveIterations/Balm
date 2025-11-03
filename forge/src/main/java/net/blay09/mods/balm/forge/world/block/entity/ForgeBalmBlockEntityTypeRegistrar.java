package net.blay09.mods.balm.forge.world.block.entity;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.entity.internal.AbstractBalmBlockEntityTypeRegistrarImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class ForgeBalmBlockEntityTypeRegistrar extends AbstractBalmBlockEntityTypeRegistrarImpl {
    public ForgeBalmBlockEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> constructor, Set<Block> blocks) {
        return new BlockEntityType<>(constructor::create, blocks);
    }
}
