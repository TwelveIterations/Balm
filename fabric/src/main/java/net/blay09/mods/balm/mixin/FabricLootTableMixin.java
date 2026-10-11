package net.blay09.mods.balm.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.fabric.loot.FabricBalmLootModifiers;
import net.blay09.mods.balm.fabric.mixin.FabricLootTableAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LootTable.class)
public abstract class FabricLootTableMixin implements FabricLootTableAccess {
    @Unique
    private ResourceKey<LootTable> balm$lootTableId;

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"))
    private void balm$applyLootModifiers(LootContext lootContext, CallbackInfoReturnable<ObjectArrayList<ItemStack>> callbackInfo) {
        final Collection<BalmLootModifier> modifiers = FabricBalmLootModifiers.getModifiers();
        if (modifiers.isEmpty()) {
            return;
        }

        FabricBalmLootModifiers.applyModifiers(modifiers, balm$lootTableId, lootContext, callbackInfo.getReturnValue());
    }

    @Override
    public void balm$setLootTableId(ResourceKey<LootTable> lootTableId) {
        this.balm$lootTableId = lootTableId;
    }
}
