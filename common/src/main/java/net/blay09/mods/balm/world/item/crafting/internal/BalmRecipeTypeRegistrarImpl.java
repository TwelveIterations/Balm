package net.blay09.mods.balm.world.item.crafting.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.crafting.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BalmRecipeTypeRegistrarImpl implements BalmRecipeTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmRecipeTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<ResourceLocation, ? extends RecipeType<TRecipe>> constructor) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.RECIPE_TYPE, identifier);
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

    private static class DeferredRecipeTypeImpl<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> implements DeferredRecipeType<TRecipeInput, TRecipe> {
        private final Holder<RecipeType<TRecipe>> type;
        private final Holder<RecipeSerializer<TRecipe>> serializer;

        private DeferredRecipeTypeImpl(
                Holder<RecipeType<TRecipe>> type,
                Holder<RecipeSerializer<TRecipe>> serializer
        ) {
            this.type = type;
            this.serializer = serializer;
        }

        @Override
        public RecipeSerializer<TRecipe> serializer() {
            if (serializer == null) {
                throw new IllegalStateException("Serializer not registered for recipe type " + type.unwrapKey().orElseThrow().location());
            }
            return serializer.value();
        }

        @Override
        public Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, ResourceKey<Recipe<?>> lastRecipe) {
            if (level instanceof ServerLevel serverLevel) {
                final var recipeManager = serverLevel.getServer().getRecipeManager();
                return recipeManager.getRecipeFor(type.value(), input, level, lastRecipe.location());
            }
            return Optional.empty();
        }

        @Override
        public Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, RecipeHolder<TRecipe> lastRecipe) {
            if (level instanceof ServerLevel serverLevel) {
                final var recipeManager = serverLevel.getServer().getRecipeManager();
                return recipeManager.getRecipeFor(type.value(), input, level, lastRecipe);
            }
            return Optional.empty();
        }

        @Override
        public RecipeType<TRecipe> type() {
            return type.value();
        }

    }

    private static class RecipeTypeRegistrationImpl<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> implements BalmRecipeTypeRegistration<TRecipeInput, TRecipe> {
        private final BalmRecipeTypeRegistrar recipeTypeRegistrar;
        private final Holder<RecipeType<TRecipe>> holder;
        private BalmRecipeSerializerRegistration<TRecipe> serializerRegistration;

        @SuppressWarnings("unchecked")
        private RecipeTypeRegistrationImpl(BalmRecipeTypeRegistrar recipeTypeRegistrar, Holder<?> holder) {
            this.recipeTypeRegistrar = recipeTypeRegistrar;
            this.holder = (Holder<@NotNull RecipeType<TRecipe>>) holder;
        }

        @Override
        public Holder<RecipeType<TRecipe>> asHolder() {
            return holder;
        }

        @Override
        public BalmRecipeTypeRegistration<TRecipeInput, TRecipe> withSerializer(Supplier<RecipeSerializer<TRecipe>> constructor) {
            final var name = holder.unwrapKey().orElseThrow().location().getPath();
            serializerRegistration = recipeTypeRegistrar.registerSerializer(name, (id) -> constructor.get());
            return this;
        }

        @Override
        public DeferredRecipeType<TRecipeInput, TRecipe> asDeferredRecipeType() {
            return new DeferredRecipeTypeImpl<>(
                    holder,
                    serializerRegistration != null ? serializerRegistration.asHolder() : null
            );
        }
    }

    private static class RecipeSerializerRegistrationImpl<T extends Recipe<?>> implements BalmRecipeSerializerRegistration<T> {
        private final Holder<RecipeSerializer<T>> holder;

        @SuppressWarnings("unchecked")
        private RecipeSerializerRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<@NotNull RecipeSerializer<T>>) holder;
        }

        @Override
        public Holder<RecipeSerializer<T>> asHolder() {
            return holder;
        }
    }

}
