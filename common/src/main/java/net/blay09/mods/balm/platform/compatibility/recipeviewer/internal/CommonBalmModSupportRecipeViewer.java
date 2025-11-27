package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerInfoProvider;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonBalmModSupportRecipeViewer implements BalmModSupportRecipeViewer {
    private final Map<Identifier, RecipeViewerInfoProvider> providers = new ConcurrentHashMap<>();

    @Override
    public void register(Identifier identifier, RecipeViewerInfoProvider provider) {
        providers.put(identifier, provider);
    }

    public Collection<RecipeViewerInfoProvider> getProviders() {
        return providers.values();
    }
}
