package com.example.balm;

import com.example.balm.capability.TestMessageCapability;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.capabilities.CapabilityType;
import net.blay09.mods.balm.platform.event.callback.InteractionEventResult;
import net.blay09.mods.balm.platform.event.callback.PlayerCallback;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class BalmExample {
    public static final String MOD_ID = "balm_example";
    public static CapabilityType<Entity, TestMessageCapability, Void> ENTITY_TEST_MESSAGE;

    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(ExampleConfig.class);
        registrars.compostables(registrar -> registrar.register(Items.DIAMOND, 1f));

        final var capabilities = Balm.capabilities();
        ENTITY_TEST_MESSAGE = capabilities.registerType(id("entity_test_message"), Entity.class, TestMessageCapability.class, Void.class);
        capabilities.registerEntityProvider(id("entity_test_message"), ENTITY_TEST_MESSAGE, (entity, ignored) -> new TestMessageCapability("This entity has the balm_example:entity_test_message capability"), () -> Set.of(EntityType.PIG));

        Balm.lootModifiers().registerLootModifier(id("loot_modifier"), new BalmLootModifier() {
            @Override
            public void apply(LootContext context, List<ItemStack> loot, @Nullable ResourceKey<LootTable> lootTableId) {
                if (lootTableId != null && lootTableId.identifier().equals(Identifier.withDefaultNamespace("blocks/grass_block"))) {
                    loot.add(new ItemStack(Items.DIAMOND));
                }
            }
        });

        PlayerCallback.InteractWithEntity.Before.EVENT.register((player, level, hand, entity) -> {
            if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                final var testMessageCapability = Balm.capabilities().getCapability(entity, ENTITY_TEST_MESSAGE);
                if (testMessageCapability != null) {
                    player.sendSystemMessage(Component.literal("Interacted with " + entity.getType() + ": " + testMessageCapability.message()));
                    return InteractionEventResult.SUCCESS_SERVER;
                }
            }
            return InteractionEventResult.DEFAULT;
        });

        System.out.println("Hello common");
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}
