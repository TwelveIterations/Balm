package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootTable.class)
public class LootTableMixin {
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"), cancellable = true)
    public void getRandomItems(LootContext lootContext, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
        var drops = callbackInfo.getReturnValue();
        var lootModifiers = ((CommonBalmLootTables) Balm.getLootTables()).lootModifiers;
        for (BalmLootModifier modifier : lootModifiers.values()) {
            modifier.apply(lootContext, drops);
        }
    }
}
