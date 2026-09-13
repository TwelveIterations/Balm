package net.blay09.mods.balm.fabric.platform.internal;

import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.blay09.mods.balm.world.entity.BalmForcedPoseHolder;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class FabricBalmHooks implements BalmHooks {

    @Override
    public boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, @Nullable Player player) {
        return BoneMealItem.growCrop(itemStack, level, pos);
    }

    @Override
    public CompoundTag getPersistentData(Entity entity) {
        var balmData = ((BalmDataHolder) entity).balm$getFabricBalmData();
        if (balmData.isEmpty()) {
            // If we have no data, try to import from NeoForge in case the world was migrated
            balmData = ((BalmDataHolder) entity).balm$getNeoForgeBalmData();
            if (!balmData.isEmpty()) {
                ((BalmDataHolder) entity).balm$setFabricBalmData(balmData);
            }
        }
        if (balmData.isEmpty()) {
            // If we still have no data, try to import from Forge in case the world was migrated
            balmData = ((BalmDataHolder) entity).balm$getForgeBalmData();
            if (!balmData.isEmpty()) {
                ((BalmDataHolder) entity).balm$setFabricBalmData(balmData);
            }
        }
        return balmData;
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainder();
    }

    @Override
    public @Nullable DyeColor getColor(ItemStack itemStack) {
        return itemStack.get(DataComponents.DYE);
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
    }

    @Override
    public void setForcedPose(Player player, @Nullable Pose pose) {
        ((BalmForcedPoseHolder) player).balm$setForcedPose(pose);
    }

}
