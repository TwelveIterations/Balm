package net.blay09.mods.balm.forge.world.inventory;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.internal.AbstractBalmMenuTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;

public class ForgeBalmMenuTypeRegistrar extends AbstractBalmMenuTypeRegistrarImpl {
    public ForgeBalmMenuTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory) {
        return new MenuType<>((IContainerFactory<TMenu>) (syncId, inventory, buf) -> factory.create(syncId,
                inventory,
                factory.getStreamCodec().decode(new RegistryFriendlyByteBuf(buf, inventory.player.registryAccess()))),
                FeatureFlagSet.of(FeatureFlags.VANILLA));
    }
}
