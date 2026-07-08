package net.blay09.mods.balm.fabric.client.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.platform.internal.BalmClientSafeClientAccess;
import net.blay09.mods.balm.fabric.client.internal.platform.runtime.internal.FabricBalmClientRuntime;
import net.blay09.mods.balm.fabric.internal.mixin.RecipeMapAccessor;
import net.blay09.mods.balm.fabric.network.internal.FabricBalmNetworking;
import net.blay09.mods.balm.network.internal.RemotePlayerModList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public class FabricBalmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ((FabricBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> FabricBalmNetworking.initializeClientHandlers());

        ClientRecipeSynchronizedEvent.EVENT.register((client, synchronizedRecipes) -> {
            if (Balm.safeClientAccess() instanceof BalmClientSafeClientAccess clientProxy) {
                ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.builder();
                ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = ImmutableMap.builder();
                synchronizedRecipes.recipes().forEach((recipe) -> {
                    final var legacyRecipeHolder = new RecipeHolder<>(recipe.id(), recipe.value());
                    byType.put(recipe.value().getType(), legacyRecipeHolder);
                    byKey.put(recipe.id(), legacyRecipeHolder);
                });
                final var recipeMap = RecipeMapAccessor.balm$create(byType.build(), byKey.build());
                clientProxy.setSyncedRecipes(recipeMap);
            }
        });

        RemotePlayerModList.RECEIVED.register(event -> RemotePlayerModList.validateRemoteMods(event.player(), event.modList()));
    }
}
