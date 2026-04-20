package net.blay09.mods.balm.neoforge.world.level.storage.loot.internal;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class NeoForgeBalmLootModifier extends LootModifier {
    public static final MapCodec<NeoForgeBalmLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance).apply(instance, NeoForgeBalmLootModifier::new));

    public NeoForgeBalmLootModifier(LootItemCondition[] conditions, int priority) {
        super(conditions, priority);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        final var lootModifiers = ((CommonBalmLootTables) Balm.lootModifiers()).lootModifiers;
        for (final var modifier : lootModifiers.values()) {
            modifier.apply(context, generatedLoot, ResourceKey.create(Registries.LOOT_TABLE, context.getQueriedLootTableId()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
