package net.blay09.mods.balm.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface BalmHooks {
    /**
     * Forge fires SaplingGrowTreeEvent, Fabric NOPs
     */
    boolean blockGrowFeature(Level level, RandomSource random, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder);

    /**
     * Forge adds player-sensitive version, Fabric uses normal version
     */
    boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, @Nullable Player player);

    default CompoundTag getPersistentData(Player player) {
        return getPersistentData((Entity) player);
    }

    /**
     * Forge provides a tag in entity data, which for players is persisted across clones and death.
     * Fabric does not provide such a tag; so we add our own
     */
    CompoundTag getPersistentData(Entity entity);

    /**
     * Forge provides a patch to cure potion effects specific to a given curative item.
     * Fabric does not provide such patch, so on Fabric this will always clear all effects.
     */
    void curePotionEffects(LivingEntity entity, ItemStack curativeItem);

    boolean isFakePlayer(Player player);

    ItemStack getCraftingRemainingItem(ItemStack itemStack);

    DyeColor getColor(ItemStack itemStack);

    boolean canItemsStack(ItemStack first, ItemStack second);

    int getBurnTime(ItemStack itemStack);

    void setBurnTime(Item item, int burnTime);

    void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix);

    boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    boolean isShield(ItemStack itemStack);

    void setForcedPose(Player player, Pose pose);

    MinecraftServer getServer();

    default double getBlockReachDistance(Player player) {
        return 4.5 + (player.isCreative() ? 0.5 : 0);
    }

    boolean isRepairable(ItemStack itemStack);
}
