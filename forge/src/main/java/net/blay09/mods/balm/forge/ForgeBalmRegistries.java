package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

public class ForgeBalmRegistries implements BalmRegistries {
    @Override
    public ResourceLocation getKey(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    @Override
    public ResourceLocation getKey(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    @Override
    public ResourceLocation getKey(Fluid fluid) {
        return ForgeRegistries.FLUIDS.getKey(fluid);
    }

    @Override
    public ResourceLocation getKey(EntityType<?> entityType) {
        return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
    }

    @Override
    public ResourceLocation getKey(MenuType<?> menuType) {
        return ForgeRegistries.MENU_TYPES.getKey(menuType);
    }

    @Override
    public Collection<ResourceLocation> getItemKeys() {
        return ForgeRegistries.ITEMS.getKeys();
    }

    @Override
    public Item getItem(ResourceLocation key) {
        return ForgeRegistries.ITEMS.getValue(key);
    }

    @Override
    public Block getBlock(ResourceLocation key) {
        return ForgeRegistries.BLOCKS.getValue(key);
    }

    @Override
    public Fluid getFluid(ResourceLocation key) {
        return ForgeRegistries.FLUIDS.getValue(key);
    }

    @Override
    public MobEffect getMobEffect(ResourceLocation key) {
        return ForgeRegistries.MOB_EFFECTS.getValue(key);
    }

    @Override
    public TagKey<Item> getItemTag(ResourceLocation key) {
        return ItemTags.create(key);
    }

    @Override
    public void enableMilkFluid() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }

    @Override
    public Attribute getAttribute(ResourceLocation key) {
        return ForgeRegistries.ATTRIBUTES.getValue(key);
    }
}
