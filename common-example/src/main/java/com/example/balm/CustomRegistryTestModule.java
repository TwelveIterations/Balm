package com.example.balm;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.platform.LoaderPlatforms;
import net.blay09.mods.balm.platform.event.callback.ServerLifecycleCallback;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class CustomRegistryTestModule implements BalmModule {
    public static final ResourceKey<Registry<ExampleSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(BalmExample.id("spells"));
    public static final Identifier SPARK_ID = BalmExample.id("spark");

    public static Registry<ExampleSpell> SPELLS;
    public static Holder<ExampleSpell> SPARK;

    @Override
    public Identifier getId() {
        return BalmExample.id("custom_registry_test");
    }

    @Override
    public void registerAdditional(BalmRegistrar registrar) {
        if (Balm.platform().name().equals(LoaderPlatforms.FORGE)) {
            return;
        }

        SPELLS = registrar.createCustomRegistry(SPELL_REGISTRY_KEY);

        final var spells = registrar.scoped(SPELL_REGISTRY_KEY, BalmExample.MOD_ID);
        SPARK = spells.register("spark", id -> new ExampleSpell("Spark"));
    }

    @Override
    public void initialize() {
        ServerLifecycleCallback.Started.EVENT.register(_ -> {
            if (SPELLS == null) {
                System.out.println("Custom registries are not supported on Forge yet.");
                return;
            }

            final var spark = SPELLS.getValue(SPARK_ID);
            System.out.println("Registered spell: " + SPARK_ID + " -> " + spark.displayName());
        });
    }

    public record ExampleSpell(String displayName) {
    }
}
