package net.blay09.mods.balm.fabric.platform.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.blay09.mods.balm.world.entity.BalmForcedPoseHolder;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class FabricBalmHooks implements BalmHooks {

    @Override
    public boolean blockGrowFeature(Level level, RandomSource random, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder) {
        return true;
    }

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
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getRecipeRemainder();
    }

    @Override
    @Nullable
    public DyeColor getColor(ItemStack itemStack) {
        if (itemStack.getItem() instanceof DyeItem) {
            return ((DyeItem) itemStack.getItem()).getDyeColor();
        }

        return null;
    }

    @Override
    public void setBurnTime(Item item, int burnTime) {
        FuelRegistryEvents.BUILD.register((builder, context) -> builder.add(item, burnTime));
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
    }

    @Override
    public boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        final var fluidTank = blockEntity != null ? Balm.capabilities().getCapability(blockEntity, hitResult.getDirection(), Objects.requireNonNull(CommonCapabilities.FLUID_TANK)) : null;
        if (fluidTank != null) {
            ItemStack handItem = player.getItemInHand(hand);
            if (handItem.getItem() == Items.BUCKET) {
                int drained = fluidTank.drain(fluidTank.getFluid(), 1000, true);
                if (drained >= 1000) {
                    Item bucketItem = fluidTank.getFluid().getBucket();
                    if (bucketItem != Items.AIR) {
                        ItemStack bucketItemStack = new ItemStack(bucketItem);
                        if (handItem.getCount() > 1) {
                            if (player.addItem(bucketItemStack)) {
                                fluidTank.getFluid().getPickupSound().ifPresent(sound -> player.playSound(sound, 1f, 1f));
                                handItem.shrink(1);
                                fluidTank.drain(fluidTank.getFluid(), 1000, false);
                                return true;
                            }
                        } else {
                            fluidTank.getFluid().getPickupSound().ifPresent(sound -> player.playSound(sound, 1f, 1f));
                            player.setItemInHand(hand, bucketItemStack);
                            fluidTank.drain(fluidTank.getFluid(), 1000, false);
                            return true;
                        }
                    }
                }
            } else {
                Fluid fluid = BuiltInRegistries.FLUID.stream().filter(it -> it.getBucket() == handItem.getItem()).findFirst().orElse(null);
                if (fluid != null && !fluid.isSame(Fluids.EMPTY)) {
                    int filled = fluidTank.fill(fluid, 1000, true);
                    if (filled >= 1000) {
                        if (handItem.getCount() > 1) {
                            ItemStack restItem = Balm.hooks().getCraftingRemainingItem(handItem);
                            if (player.addItem(restItem)) {
                                player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
                                fluidTank.getFluid().getPickupSound().ifPresent(sound -> player.playSound(sound, 1f, 1f));
                                handItem.shrink(1);
                                fluidTank.fill(fluid, 1000, false);
                                return true;
                            }
                        } else {
                            player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
                            player.setItemInHand(hand, Balm.hooks().getCraftingRemainingItem(handItem));
                            fluidTank.fill(fluid, 1000, false);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void setForcedPose(Player player, @Nullable Pose pose) {
        ((BalmForcedPoseHolder) player).balm$setForcedPose(pose);
    }

}
