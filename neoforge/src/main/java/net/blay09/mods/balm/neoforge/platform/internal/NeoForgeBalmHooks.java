package net.blay09.mods.balm.neoforge.platform.internal;

import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.blay09.mods.balm.platform.BalmHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeBalmHooks implements BalmHooks {

    public final Map<Item, Integer> burnTimes = new HashMap<>();

    public NeoForgeBalmHooks() {
        NeoForge.EVENT_BUS.addListener(this::furnaceFuelBurnTime);
    }

    private void furnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        final var found = burnTimes.get(event.getItemStack().getItem());
        if (found != null) {
            event.setBurnTime(found);
        }
    }

    @Override
    public boolean blockGrowFeature(Level level, RandomSource random, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder) {
        return !EventHooks.fireBlockGrowFeature(level, random, pos, holder).isCanceled();
    }

    @Override
    public boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, Player player) {
        if (player != null) {
            return BoneMealItem.applyBonemeal(itemStack, level, pos, player);
        } else {
            return BoneMealItem.growCrop(itemStack, level, pos);
        }
    }

    @Override
    public CompoundTag getPersistentData(Entity entity) {
        CompoundTag persistentData = entity.getPersistentData();
        if (entity instanceof Player) {
            CompoundTag persistedTag = persistentData.getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
            persistentData.put(Player.PERSISTED_NBT_TAG, persistedTag);
            persistentData = persistedTag;
        }

        CompoundTag balmData = persistentData.getCompoundOrEmpty("BalmData");
        if (balmData.isEmpty()) {
            // If we have no data, try to import from Fabric in case the world was migrated
            balmData = ((BalmDataHolder) entity).balm$getFabricBalmData();
        }
        if (balmData.isEmpty()) {
            // If we still have no data, try to import from Forge in case the world was migrated
            balmData = ((BalmDataHolder) entity).balm$getForgeBalmData();
        }
        if (!balmData.isEmpty()) {
            persistentData.put("BalmData", balmData);
        }

        return balmData;
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainder();
    }

    @Override
    public DyeColor getColor(ItemStack itemStack) {
        return DyeColor.getColor(itemStack);
    }

    @Override
    public void setBurnTime(Item item, int burnTime) {
        burnTimes.put(item, burnTime);
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        EventHooks.firePlayerCraftingEvent(player, crafted, craftMatrix);
    }

    @Override
    public boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
    }

    @Override
    public void setForcedPose(Player player, Pose pose) {
        player.setForcedPose(pose);
    }

}
