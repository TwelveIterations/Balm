package net.blay09.mods.balm.fabric.world.level.block.entity.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.entity.internal.AbstractBalmBlockEntityTypeRegistrarImpl;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class FabricBalmBlockEntityTypeRegistrar extends AbstractBalmBlockEntityTypeRegistrarImpl {
    public FabricBalmBlockEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> constructor, Set<Block> blocks) {
        return FabricBlockEntityTypeBuilder.create(constructor::create).addBlocks(blocks).build();
    }
}
