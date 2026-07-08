package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGlobalGuiHandler;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerOcclusionProvider;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.CommonBalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.IdentifiableRecipeTypeTransferRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.ScreenOcclusionRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class CommonJeiPlugin implements IModPlugin {
    private final JeiRecipeViewerRegistrar registrar = new JeiRecipeViewerRegistrar();
    private boolean registrarsInitialized;

    private void ensureInitialized() {
        if (!registrarsInitialized) {
            if (Balm.modSupport().recipeViewers() instanceof CommonBalmModSupportRecipeViewer recipeViewerSupport) {
                for (final var provider : recipeViewerSupport.getProviders()) {
                    provider.initialize(registrar);
                }
            }
            registrarsInitialized = true;
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("balm", "jei");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ensureInitialized();

        for (final var recipeTypeRegistration : registrar.getRecipeTypes()) {
            recipeTypeRegistration.registerCatalysts(registration);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ensureInitialized();

        for (final var recipeTypeRegistration : registrar.getRecipeTypes()) {
            recipeTypeRegistration.registerRecipes(registration);
        }

        for (final var ingredientInfoRegistration : registrar.getIngredientInfoRegistrations()) {
            registration.addIngredientInfo(ingredientInfoRegistration.itemLike(), ingredientInfoRegistration.description());
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        ensureInitialized();

        for (final var recipeTypeRegistration : registrar.getRecipeTypes()) {
            recipeTypeRegistration.registerCategories(registration);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        ensureInitialized();

        for (final var entry : registrar.getScreenOcclusions()) {
            registerScreenOcclusion(registration, entry);
        }

        for (final var entry : registrar.getGlobalScreenOcclusions()) {
            registerGlobalScreenOcclusion(registration, entry);
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        ensureInitialized();

        for (final var entry : registrar.getIdentifiableRecipeTypeTransferRegistrations()) {
            registerRecipeTransferHandler(registration, entry);
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        if (Balm.modSupport().recipeViewers() instanceof CommonBalmModSupportRecipeViewer recipeViewerSupport) {
            recipeViewerSupport.setHasKeyboardFocus(() -> jeiRuntime.getIngredientListOverlay().hasKeyboardFocus());
        }
    }

    @Override
    public void onRuntimeUnavailable() {
        if (Balm.modSupport().recipeViewers() instanceof CommonBalmModSupportRecipeViewer recipeViewerSupport) {
            recipeViewerSupport.setHasKeyboardFocus(null);
        }
    }

    private <TMenu extends AbstractContainerMenu> void registerRecipeTransferHandler(IRecipeTransferRegistration registration, IdentifiableRecipeTypeTransferRegistration<TMenu> entry) {
        registration.getJeiHelpers().getRecipeType(entry.recipeTypeId()).ifPresent(recipeType -> registration.addRecipeTransferHandler(entry.menuClass(),
                entry.menuType().value(),
                recipeType,
                entry.recipeSlotStart(),
                entry.recipeSlotCount(),
                entry.inventorySlotStart(),
                entry.inventorySlotCount()));
    }

    private <T extends AbstractContainerScreen<?>> void registerScreenOcclusion(IGuiHandlerRegistration registration, ScreenOcclusionRegistration<T> entry) {
        registration.addGuiContainerHandler(entry.containerScreenClass(), new IGuiContainerHandler<>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(T containerScreen) {
                return entry.provider().getOcclusions(containerScreen);
            }
        });
    }

    private <T extends AbstractContainerScreen<?>> void registerGlobalScreenOcclusion(IGuiHandlerRegistration registration, RecipeViewerOcclusionProvider<T> entry) {
        registration.addGlobalGuiHandler(new IGlobalGuiHandler() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Rect2i> getGuiExtraAreas() {
                if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> containerScreen) {
                    return entry.getOcclusions((T) containerScreen);
                }

                return Collections.emptyList();
            }
        });
    }
}
