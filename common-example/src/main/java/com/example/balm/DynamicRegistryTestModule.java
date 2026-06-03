package com.example.balm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.platform.event.callback.ServerLifecycleCallback;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class DynamicRegistryTestModule implements BalmModule {
    public static final ResourceKey<Registry<ExampleSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(BalmExample.id("dynamic_spells"));
    public static final Identifier SPARK_ID = BalmExample.id("spark");

    @Override
    public Identifier getId() {
        return BalmExample.id("dynamic_registry_test");
    }

    @Override
    public void registerAdditional(BalmRegistrar registrar) {
        registrar.createDynamicRegistry(SPELL_REGISTRY_KEY, ExampleSpell.CODEC);
    }

    @Override
    public void initialize() {
        ServerLifecycleCallback.Started.EVENT.register(server -> {
            final var spells = server.registryAccess().lookupOrThrow(SPELL_REGISTRY_KEY);
            final var spark = spells.getValue(SPARK_ID);
            System.out.println("Loaded dynamic spell: " + SPARK_ID + " -> " + spark.displayName());
        });
    }

    public record ExampleSpell(String displayName) {
        public static final Codec<ExampleSpell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("display_name").forGetter(ExampleSpell::displayName)
        ).apply(instance, ExampleSpell::new));
    }
}
