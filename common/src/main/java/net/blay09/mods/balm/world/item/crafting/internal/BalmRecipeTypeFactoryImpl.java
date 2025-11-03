package net.blay09.mods.balm.world.item.crafting.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.crafting.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BalmRecipeTypeFactoryImpl implements BalmRecipeTypeFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmRecipeTypeFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<ResourceLocation, RecipeType<TRecipe>> constructor) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.RECIPE_TYPE, resourceLocation);
        final var holder = registrar.register(resourceKey, constructor::apply);
        return new RecipeTypeRegistrationImpl<>(this, holder);
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeSerializerRegistration<TRecipe> registerSerializer(String name, Function<ResourceLocation, RecipeSerializer<TRecipe>> constructor) {
        final var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.RECIPE_SERIALIZER, id);
        final var holder = registrar.register(key, constructor::apply);
        return new RecipeSerializerRegistrationImpl<>(holder);
    }

    @Override
    public BalmRecipeBookCategoryRegistration registerBookCategory(String name, Function<ResourceLocation, RecipeBookCategory> constructor) {
        final var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.RECIPE_BOOK_CATEGORY, id);
        final var holder = registrar.register(key, constructor::apply);
        return new RecipeBookCategoryRegistrationImpl(holder);
    }

    @Override
    public <T extends RecipeDisplay.Type<?>> BalmRecipeDisplayTypeRegistration<T> registerDisplayType(String name, Function<ResourceLocation, T> constructor) {
        final var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.RECIPE_DISPLAY, id);
        final var holder = registrar.register(key, constructor::apply);
        return new RecipeDisplayTypeRegistrationImpl<>(holder);
    }

    @Override
    public <T extends SlotDisplay.Type<?>> BalmSlotDisplayTypeRegistration<T> registerSlotDisplayType(String name, Function<ResourceLocation, T> constructor) {
        final var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.SLOT_DISPLAY, id);
        final var holder = registrar.register(key, constructor::apply);
        return new SlotDisplayTypeRegistrationImpl<>(holder);
    }

    private record DeferredRecipeTypeImpl<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>>(
            Holder<RecipeType<TRecipe>> type,
            @Nullable Holder<RecipeSerializer<TRecipe>> serializer,
            @Nullable Holder<RecipeBookCategory> bookCategory
    ) implements DeferredRecipeType<TRecipeInput, TRecipe> {
        @Override
        public Holder<RecipeSerializer<TRecipe>> serializer() {
            if (serializer == null) {
                throw new IllegalStateException("Serializer not registered for recipe type " + type.unwrapKey().orElseThrow().location());
            }
            return serializer;
        }

        @Override
        public Holder<RecipeBookCategory> bookCategory() {
            if (serializer == null) {
                throw new IllegalStateException("Book category not registered for recipe type " + type.unwrapKey().orElseThrow().location());
            }
            return bookCategory;
        }

        @Override
        public Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, @Nullable ResourceKey<Recipe<?>> lastRecipe) {
            if (level instanceof ServerLevel serverLevel) {
                final var recipeManager = serverLevel.getServer().getRecipeManager();
                return recipeManager.getRecipeFor(type.value(), input, level, lastRecipe);
            }
            return Optional.empty();
        }

        @Override
        public Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, @Nullable RecipeHolder<TRecipe> lastRecipe) {
            if (level instanceof ServerLevel serverLevel) {
                final var recipeManager = serverLevel.getServer().getRecipeManager();
                return recipeManager.getRecipeFor(type.value(), input, level, lastRecipe);
            }
            return Optional.empty();
        }
    }

    private static class RecipeTypeRegistrationImpl<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> implements BalmRecipeTypeRegistration<TRecipeInput, TRecipe> {
        private final BalmRecipeTypeFactory factory;
        private final Holder<RecipeType<TRecipe>> holder;
        @Nullable
        private BalmRecipeSerializerRegistration<TRecipe> serializerRegistration;
        @Nullable
        private BalmRecipeBookCategoryRegistration bookCategoryRegistration;

        @SuppressWarnings("unchecked")
        private RecipeTypeRegistrationImpl(BalmRecipeTypeFactory factory, Holder<?> holder) {
            this.factory = factory;
            this.holder = (Holder<RecipeType<TRecipe>>) holder;
        }

        @Override
        public Holder<RecipeType<TRecipe>> asHolder() {
            return holder;
        }

        @Override
        public BalmRecipeTypeRegistration<TRecipeInput, TRecipe> withSerializer(Supplier<RecipeSerializer<TRecipe>> constructor) {
            final var name = holder.unwrapKey().orElseThrow().location().getPath();
            serializerRegistration = factory.registerSerializer(name, (id) -> constructor.get());
            return this;
        }

        @Override
        public BalmRecipeTypeRegistration<TRecipeInput, TRecipe> withRecipeBookCategory() {
            final var name = holder.unwrapKey().orElseThrow().location().getPath();
            bookCategoryRegistration = factory.registerBookCategory(name, id -> new RecipeBookCategory());
            return this;
        }

        @Override
        public DeferredRecipeType<TRecipeInput, TRecipe> asDeferredRecipeType() {
            return new DeferredRecipeTypeImpl<>(
                    holder,
                    serializerRegistration != null ? serializerRegistration.asHolder() : null,
                    bookCategoryRegistration != null ? bookCategoryRegistration.asHolder() : null
            );
        }
    }

    private static class RecipeSerializerRegistrationImpl<T extends Recipe<?>> implements BalmRecipeSerializerRegistration<T> {
        private final Holder<RecipeSerializer<T>> holder;

        @SuppressWarnings("unchecked")
        private RecipeSerializerRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<RecipeSerializer<T>>) holder;
        }

        @Override
        public Holder<RecipeSerializer<T>> asHolder() {
            return holder;
        }
    }

    private static class RecipeBookCategoryRegistrationImpl implements BalmRecipeBookCategoryRegistration {
        private final Holder<RecipeBookCategory> holder;

        private RecipeBookCategoryRegistrationImpl(Holder<RecipeBookCategory> holder) {
            this.holder = holder;
        }

        @Override
        public Holder<RecipeBookCategory> asHolder() {
            return holder;
        }
    }

    private static class RecipeDisplayTypeRegistrationImpl<T extends RecipeDisplay.Type<?>> implements BalmRecipeDisplayTypeRegistration<T> {
        private final Holder<T> holder;

        @SuppressWarnings("unchecked")
        private RecipeDisplayTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<T>) holder;
        }

        @Override
        public Holder<T> asHolder() {
            return holder;
        }
    }

    private static class SlotDisplayTypeRegistrationImpl<T extends SlotDisplay.Type<?>> implements BalmSlotDisplayTypeRegistration<T> {
        private final Holder<T> holder;

        @SuppressWarnings("unchecked")
        private SlotDisplayTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<T>) holder;
        }

        @Override
        public Holder<T> asHolder() {
            return holder;
        }
    }
}
