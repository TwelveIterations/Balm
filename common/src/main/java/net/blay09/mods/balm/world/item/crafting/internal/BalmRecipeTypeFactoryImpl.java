package net.blay09.mods.balm.world.item.crafting.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.crafting.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.Nullable;

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
    public <T extends Recipe<?>> BalmRecipeTypeRegistration<T> registerType(String name, Function<ResourceLocation, RecipeType<T>> constructor) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.RECIPE_TYPE, resourceLocation);
        final var holder = registrar.register(resourceKey, constructor::apply);
        return new RecipeTypeRegistrationImpl<>(this, holder);
    }

    @Override
    public <T extends Recipe<?>> BalmRecipeSerializerRegistration<T> registerSerializer(String name, Function<ResourceLocation, RecipeSerializer<T>> constructor) {
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

    private record DeferredRecipeTypeImpl<T extends Recipe<?>>(Holder<RecipeType<T>> type,
                                                               @Nullable Holder<RecipeSerializer<T>> serializer,
                                                               @Nullable Holder<RecipeBookCategory> bookCategory) implements DeferredRecipeType<T> {
        @Override
        public Holder<RecipeSerializer<T>> serializer() {
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
    }

    private static class RecipeTypeRegistrationImpl<T extends Recipe<?>> implements BalmRecipeTypeRegistration<T> {
        private final BalmRecipeTypeFactory factory;
        private final Holder<RecipeType<T>> holder;
        @Nullable
        private BalmRecipeSerializerRegistration<T> serializerRegistration;
        @Nullable
        private BalmRecipeBookCategoryRegistration bookCategoryRegistration;

        @SuppressWarnings("unchecked")
        private RecipeTypeRegistrationImpl(BalmRecipeTypeFactory factory, Holder<?> holder) {
            this.factory = factory;
            this.holder = (Holder<RecipeType<T>>) holder;
        }

        @Override
        public Holder<RecipeType<T>> asHolder() {
            return holder;
        }

        @Override
        public BalmRecipeTypeRegistration<T> withSerializer(Supplier<RecipeSerializer<T>> constructor) {
            final var name = holder.unwrapKey().orElseThrow().location().getPath();
            serializerRegistration = factory.registerSerializer(name, (id) -> constructor.get());
            return this;
        }

        @Override
        public BalmRecipeTypeRegistration<T> withRecipeBookCategory() {
            final var name = holder.unwrapKey().orElseThrow().location().getPath();
            bookCategoryRegistration = factory.registerBookCategory(name, id -> new RecipeBookCategory());
            return this;
        }

        @Override
        public DeferredRecipeType<T> asDeferredRecipeType() {
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
