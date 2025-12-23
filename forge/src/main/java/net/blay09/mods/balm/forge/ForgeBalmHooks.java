package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.entity.BalmEntity;
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
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ForgeBalmHooks implements BalmHooks {

    public final Map<Item, Integer> burnTimes = new ConcurrentHashMap<>();

    public ForgeBalmHooks() {
        MinecraftForge.EVENT_BUS.addListener(this::furnaceFuelBurnTime);
    }

    private void furnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        final var found = burnTimes.get(event.getItemStack().getItem());
        if (found != null) {
            event.setBurnTime(found);
        }
    }

    @Override
    public boolean blockGrowFeature(Level level, RandomSource random, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder) {
        return !ForgeEventFactory.blockGrowFeature(level, random, pos, holder).getResult().equals(Event.Result.DENY);
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
            CompoundTag persistedTag = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
            persistentData.put(Player.PERSISTED_NBT_TAG, persistedTag);
            persistentData = persistedTag;
        }

        CompoundTag balmData = persistentData.getCompound("BalmData");
        if (balmData.size() == 0) {
            // If we have no data, try to import from Fabric in case the world was migrated
            balmData = ((BalmEntity) entity).getFabricBalmData();
        }
        persistentData.put("BalmData", balmData);

        return balmData;
    }

    @Override
    public void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.curePotionEffects(curativeItem);
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainingItem();
    }

    @Override
    public DyeColor getColor(ItemStack itemStack) {
        return DyeColor.getColor(itemStack);
    }

    @Override
    public boolean canItemsStack(ItemStack first, ItemStack second) {
        return ItemHandlerHelper.canItemStacksStack(first, second);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING);
    }

    @Override
    public void setBurnTime(Item item, int burnTime) {
        burnTimes.put(item, burnTime);
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        ForgeEventFactory.firePlayerCraftingEvent(player, crafted, craftMatrix);
    }

    @Override
    public boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
    }

    @Override
    public boolean isShield(ItemStack itemStack) {
        return itemStack.getItem().canPerformAction(itemStack, ToolActions.SHIELD_BLOCK);
    }

    @Override
    public boolean isRepairable(ItemStack itemStack) {
        return itemStack.isRepairable();
    }

    @Override
    public void setForcedPose(Player player, Pose pose) {
        player.setForcedPose(pose);
    }

    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public double getBlockReachDistance(Player player) {
        return player.getBlockReach();
    }
}
