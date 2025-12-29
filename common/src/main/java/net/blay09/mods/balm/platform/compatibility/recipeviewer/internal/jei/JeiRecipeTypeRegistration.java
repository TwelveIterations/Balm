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
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

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
    private ItemStack icon = ItemStack.EMPTY;
    private BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder = (recipe, builder) -> {
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
        final var drawableIcon = icon != null ? guiHelper.createDrawableItemStack(icon) : null;
        final var drawableBackground = backgroundTexture != null ? guiHelper.createDrawable(backgroundTexture, backgroundTextureX, backgroundTextureY, width, height) : null;
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
            return this;
        }

        @Override
        public RecipeViewerDisplayBuilder<T> background(Identifier texture, int u, int v) {
            JeiRecipeTypeRegistration.this.backgroundTexture = texture;
            JeiRecipeTypeRegistration.this.backgroundTextureX = u;
            JeiRecipeTypeRegistration.this.backgroundTextureY = v;
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
