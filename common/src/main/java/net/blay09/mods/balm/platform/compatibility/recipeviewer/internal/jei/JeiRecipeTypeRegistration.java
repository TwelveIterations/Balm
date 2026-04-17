package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplayBuilder;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotsBuilder;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerRecipeTypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class JeiRecipeTypeRegistration<T> implements RecipeViewerRecipeTypeRegistration<T> {
    protected final IRecipeType<T> jeiRecipeType;
    private final List<ItemStack> craftingStations = new ArrayList<>();
    private final List<T> recipes = new ArrayList<>();

    private Component title = Component.empty();
    private int width;
    private int height;
    @Nullable
    private Identifier backgroundTexture;
    private int backgroundTextureX;
    private int backgroundTextureY;
    private int backgroundWidth;
    private int backgroundHeight;
    private int backgroundTextureWidth = 256;
    private int backgroundTextureHeight = 256;
    private ItemStack icon = ItemStack.EMPTY;
    private BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder = (_, _) -> {
    };

    public JeiRecipeTypeRegistration(Identifier identifier, Class<T> recipeClass) {
        this.jeiRecipeType = IRecipeType.create(identifier, recipeClass);
    }

    @Override
    public RecipeViewerRecipeTypeRegistration<T> withCraftingStation(ItemStack itemStack) {
        craftingStations.add(itemStack);
        return this;
    }

    @Override
    public RecipeViewerRecipeTypeRegistration<T> withRecipe(T recipe) {
        this.recipes.add(recipe);
        return this;
    }

    @Override
    public RecipeViewerRecipeTypeRegistration<T> withRecipes(Collection<T> recipes) {
        this.recipes.addAll(recipes);
        return this;
    }

    @Override
    public void buildDisplay(Consumer<RecipeViewerDisplayBuilder<T>> builder) {
        builder.accept(new JeiRecipeViewerDisplayBuilder());
    }

    private IRecipeCategory<T> createJeiCategory(IJeiHelpers helpers) {
        final var guiHelper = helpers.getGuiHelper();
        final var drawableIcon = !icon.isEmpty() ? guiHelper.createDrawableItemStack(icon) : null;
        final var drawableBackground = backgroundTexture != null ? guiHelper.drawableBuilder(backgroundTexture, backgroundTextureX, backgroundTextureY, backgroundWidth, backgroundHeight).setTextureSize(backgroundTextureWidth, backgroundTextureHeight).build() : null;
        return new CommonJeiRecipeCategory<>(jeiRecipeType, title, drawableIcon, width, height, drawableBackground, slotsBuilder);
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(createJeiCategory(registration.getJeiHelpers()));
    }

    public void registerCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(jeiRecipeType, craftingStations.toArray(ItemStack[]::new));
    }

    public void registerRecipes(IRecipeRegistration registration) {
        if (!recipes.isEmpty()) {
            registration.addRecipes(jeiRecipeType, recipes);
        }
    }

    private class JeiRecipeViewerDisplayBuilder implements RecipeViewerDisplayBuilder<T> {
        @Override
        public RecipeViewerDisplayBuilder<T> size(int width, int height) {
            JeiRecipeTypeRegistration.this.width = width;
            JeiRecipeTypeRegistration.this.height = height;
            if (backgroundWidth == 0) {
                backgroundWidth = width;
            }
            if (backgroundHeight == 0) {
                backgroundHeight = height;
            }
            return this;
        }

        @Override
        public RecipeViewerDisplayBuilder<T> background(Identifier texture, int u, int v) {
            return background(texture, u, v, width, height);
        }

        @Override
        public RecipeViewerDisplayBuilder<T> background(Identifier texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
            JeiRecipeTypeRegistration.this.backgroundTexture = texture;
            JeiRecipeTypeRegistration.this.backgroundTextureX = u;
            JeiRecipeTypeRegistration.this.backgroundTextureY = v;
            JeiRecipeTypeRegistration.this.backgroundWidth = width;
            JeiRecipeTypeRegistration.this.backgroundHeight = height;
            JeiRecipeTypeRegistration.this.backgroundTextureWidth = textureWidth;
            JeiRecipeTypeRegistration.this.backgroundTextureHeight = textureHeight;
            return this;
        }

        @Override
        public RecipeViewerDisplayBuilder<T> icon(ItemStack itemStack) {
            JeiRecipeTypeRegistration.this.icon = itemStack;
            return this;
        }

        @Override
        public RecipeViewerDisplayBuilder<T> title(Component title) {
            JeiRecipeTypeRegistration.this.title = title;
            return this;
        }

        @Override
        public RecipeViewerDisplayBuilder<T> slots(BiConsumer<T, RecipeViewerDisplaySlotsBuilder> builder) {
            JeiRecipeTypeRegistration.this.slotsBuilder = builder;
            return this;
        }
    }
}
