package com.example.balm;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.CapabilityType;
import net.blay09.mods.balm.platform.event.callback.InteractionEventResult;
import net.blay09.mods.balm.platform.event.callback.PlayerCallback;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Set;

public class EntityCapabilityTestModule implements BalmModule {
    public static CapabilityType<Entity, TestMessageCapability, Void> ENTITY_TEST_MESSAGE;

    @Override
    public Identifier getId() {
        return BalmExample.id("entity_capability_test");
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        ENTITY_TEST_MESSAGE = capabilities.registerType(BalmExample.id("entity_test_message"), Entity.class, TestMessageCapability.class, Void.class);
        capabilities.registerEntityProvider(BalmExample.id("entity_test_message"),
                ENTITY_TEST_MESSAGE,
                (entity, ignored) -> new TestMessageCapability("This entity has the balm_example:entity_test_message capability"),
                () -> Set.of(EntityType.PIG));
    }

    @Override
    public void initialize() {
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
    }

    public record TestMessageCapability(String message) {
    }
}
