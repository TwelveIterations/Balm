package net.blay09.mods.balm.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.function.Function;

public interface BalmRegistries {
    /**
     * @deprecated Use BuiltInRegistries.ITEM.getKey(item) instead.
     */
    @Deprecated
    default ResourceLocation getKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    /**
     * @deprecated Use BuiltInRegistries.BLOCK.getKey(block) instead.
     */
    @Deprecated
    default ResourceLocation getKey(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    /**
     * @deprecated Use BuiltInRegistries.FLUID.getKey(fluid) instead.
     */
    @Deprecated
    default ResourceLocation getKey(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }

    /**
     * @deprecated Use BuiltInRegistries.ENTITY_TYPE.getKey(mobEffect) instead.
     */
    @Deprecated
    default ResourceLocation getKey(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }

    /**
     * @deprecated Use BuiltInRegistries.MENU.getKey(menuType) instead.
     */
    @Deprecated
    default ResourceLocation getKey(MenuType<?> menuType) {
        return BuiltInRegistries.MENU.getKey(menuType);
    }

    /**
     * @deprecated Use BuiltInRegistries.ITEM.keySet() instead.
     */
    @Deprecated
    default Collection<ResourceLocation> getItemKeys() {
        return BuiltInRegistries.ITEM.keySet();
    }

    /**
     * @deprecated Use BuiltInRegistries.ITEM.get(key) instead.
     */
    @Deprecated
    default Item getItem(ResourceLocation key) {
        return BuiltInRegistries.ITEM.get(key);
    }

    /**
     * @deprecated Use BuiltInRegistries.BLOCK.get(key) instead.
     */
    @Deprecated
    default Block getBlock(ResourceLocation key) {
        return BuiltInRegistries.BLOCK.get(key);
    }

    /**
     * @deprecated Use BuiltInRegistries.FLUID.get(key) instead.
     */
    @Deprecated
    default Fluid getFluid(ResourceLocation key) {
        return BuiltInRegistries.FLUID.get(key);
    }

    /**
     * @deprecated Use BuiltInRegistries.MOB_EFFECT.get(key) instead.
     */
    @Deprecated
    default MobEffect getMobEffect(ResourceLocation key) {
        return BuiltInRegistries.MOB_EFFECT.get(key);
    }

    /**
     * @deprecated Use TagKey.create(Registries.ITEM, key) instead.
     */
    @Deprecated
    default TagKey<Item> getItemTag(ResourceLocation key) {
        return TagKey.create(Registries.ITEM, key);
    }

    /**
     * @deprecated Use BuiltInRegistries.ATTRIBUTE.get(key) instead.
     */
    @Deprecated
    default Attribute getAttribute(ResourceLocation key) {
        return BuiltInRegistries.ATTRIBUTE.get(key);
    }

    void enableMilkFluid();

    Fluid getMilkFluid();

    <T> DeferredObject<T> register(Registry<T> registryId, Function<ResourceLocation, T> supplier, ResourceLocation identifier);
}
