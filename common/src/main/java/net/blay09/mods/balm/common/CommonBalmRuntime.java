package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.blay09.mods.balm.common.proxy.ModProxyImpl;
import net.blay09.mods.balm.common.proxy.PlatformProxyImpl;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.core.component.internal.BalmDataComponentTypeRegistrarImpl;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.item.crafting.internal.BalmRecipeTypeRegistrarImpl;
import net.blay09.mods.balm.world.item.internal.BalmItemRegistrarImpl;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.internal.BalmBlockRegistrarImpl;
import net.minecraft.SharedConstants;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CommonBalmRuntime<TLoadContext extends BalmRuntimeLoadContext> implements BalmRuntime<TLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final Supplier<BalmProxy> proxy = this.<BalmProxy>sidedProxy("net.blay09.mods.balm.api.BalmProxy", "net.blay09.mods.balm.api.client.BalmClientProxy").buildLazily();

    private boolean ready;

    @Override
    public BalmProxy getProxy() {
        return proxy.get();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        if (isReady()) {
            callback.run();
        }
    }

    @Override
    public void registerModule(BalmModule module) {
        modules.add(module);
        initializeModule(module);
    }

    @Override
    public void registerModule(BalmRegistrars registrars, BalmModule module) {
        modules.add(module);
        initializeModule(module);
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return new SidedProxy<>(this::getEnvironment, commonName, clientName);
    }

    @Override
    public <T> PlatformProxy<T> platformProxy() {
        return new PlatformProxyImpl<>(getPlatform());
    }

    @Override
    public <T> ModProxy<T> modProxy() {
        return new ModProxyImpl<>(this::getModVersion);
    }

    protected abstract Optional<String> getModVersion(String modId);

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }

        registerModule(new BaseModule());
        registerModule(new ConfigSync());
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            try {
                Class.forName(className).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.IS_RUNNING_IN_IDE;
    }


    @Override
    public void blocks(String namespace, Consumer<BalmBlockRegistrar> initializer) {
        initializer.accept(new BalmBlockRegistrarImpl(registrar(), namespace));
    }

    @Override
    public void items(String namespace, Consumer<BalmItemRegistrar> initializer) {
        initializer.accept(new BalmItemRegistrarImpl(registrar(), namespace));
    }

    @Override
    public void recipeTypes(String namespace, Consumer<BalmRecipeTypeRegistrar> initializer) {
        initializer.accept(new BalmRecipeTypeRegistrarImpl(registrar(), namespace));
    }

    @Override
    public void dataComponentTypes(String namespace, Consumer<BalmDataComponentTypeRegistrar> initializer) {
        initializer.accept(new BalmDataComponentTypeRegistrarImpl(registrar(), namespace));
    }
}
