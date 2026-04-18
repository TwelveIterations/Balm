package net.blay09.mods.balm.forge.world.level.storage.loot.internal;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class ForgeBalmLootModifier extends LootModifier {
    public static final MapCodec<ForgeBalmLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance).apply(instance, ForgeBalmLootModifier::new));

    public ForgeBalmLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(LootTable lootTable, ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        final var lootModifiers = ((CommonBalmLootTables) Balm.lootModifiers()).lootModifiers;
        for (final var modifier : lootModifiers.values()) {
            modifier.apply(context, generatedLoot);
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
