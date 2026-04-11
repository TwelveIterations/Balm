package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerInfoProvider;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CommonBalmModSupportRecipeViewer implements BalmModSupportRecipeViewer {
    private final Map<Identifier, RecipeViewerInfoProvider> providers = new ConcurrentHashMap<>();
    private @Nullable Supplier<Boolean> hasKeyboardFocus;

    @Override
    public void register(Identifier identifier, RecipeViewerInfoProvider provider) {
        providers.put(identifier, provider);
    }

    @Override
    public boolean hasKeyboardFocus() {
        return hasKeyboardFocus != null && hasKeyboardFocus.get();
    }

    public Collection<RecipeViewerInfoProvider> getProviders() {
        return providers.values();
    }

    public void setHasKeyboardFocus(@Nullable Supplier<Boolean> hasKeyboardFocus) {
        this.hasKeyboardFocus = hasKeyboardFocus;
    }
}
