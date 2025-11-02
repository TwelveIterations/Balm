package net.blay09.mods.balm.neoforge.level.block.entity;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.entity.AbstractBalmBlockEntityTypeFactoryImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class NeoForgeBalmBlockEntityTypeFactory extends AbstractBalmBlockEntityTypeFactoryImpl {
    public NeoForgeBalmBlockEntityTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<?> createBlockEntityType(BlockEntitySupplier<T> constructor, Set<Block> blocks) {
        return new BlockEntityType<>(constructor::create, blocks);
    }
}
