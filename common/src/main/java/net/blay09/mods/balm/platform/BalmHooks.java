package net.blay09.mods.balm.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

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
     * Fabric does not provide such a tag; so we add our own.
     */
    CompoundTag getPersistentData(Entity entity);

    /**
     * Checks whether the given player is a fake player.
     *
     * <li>On Fabric and NeoForge, returns true if the player is an instance of their respective FakePlayer class</li>
     * <li>On Forge, always returns false as they no longer have a FakePlayer class.</li>
     *
     * @return <code>true</code> if the player is fake
     */
    boolean isFakePlayer(Player player);

    @Nullable
    ItemStackTemplate getCraftingRemainingItem(ItemStack itemStack);

    /**
     * Returns the dye color for a dye item.
     *
     * <li>On Fabric, only returns a dye color if the item extends {@link net.minecraft.world.item.DyeItem}</li>
     * <li>On NeoForge and Forge, also returns a dye color if the item has any of the dye tags.</li>
     */
    @Nullable
    DyeColor getColor(ItemStack itemStack);

    void setBurnTime(Item item, int burnTime);

    void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix);

    /**
     * @deprecated This method's implementation is fairly old and has not been updated to modern transfer APIs.
     */
    @Deprecated
    boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    void setForcedPose(Player player, @Nullable Pose pose);

}
