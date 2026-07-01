package net.blay09.mods.balm.platform.runtime.internal;

import net.blay09.mods.balm.platform.BalmSafeClientAccess;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.blay09.mods.balm.platform.ModProxy;
import net.blay09.mods.balm.platform.PlatformProxy;
import net.blay09.mods.balm.platform.SidedProxy;
import net.blay09.mods.balm.platform.config.internal.ConfigSync;
import net.blay09.mods.balm.platform.internal.ModProxyImpl;
import net.blay09.mods.balm.platform.internal.PlatformProxyImpl;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.internal.EventMapperImpl;
import net.blay09.mods.balm.platform.module.internal.InternalsModule;
import net.blay09.mods.balm.world.entity.ai.village.poi.BalmPoiTypeRegistrar;
import net.blay09.mods.balm.world.entity.ai.village.poi.internal.BalmPoiTypeRegistrarImpl;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.internal.BalmItemRegistrarImpl;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.item.crafting.internal.BalmRecipeTypeRegistrarImpl;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.core.component.internal.BalmDataComponentTypeRegistrarImpl;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.internal.BalmBlockRegistrarImpl;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CommonBalmRuntime<TLoadContext extends BalmRuntimeLoadContext> implements BalmRuntime<TLoadContext> {

    private static final Logger logger = LoggerFactory.getLogger(CommonBalmRuntime.class);

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final Supplier<BalmSafeClientAccess> proxy = this.<BalmSafeClientAccess>sidedProxy("net.blay09.mods.balm.platform.BalmSafeClientAccess",
            "net.blay09.mods.balm.client.platform.internal.BalmClientSafeClientAccess").buildLazily();

    private boolean ready;

    @Override
    public BalmSafeClientAccess getProxy() {
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
    public void registerModule(BalmRegistrars registrars, BalmModule module) {
        modules.add(module);
        initializeModule(module);
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return new SidedProxy<>(() -> platform().physicalSide(), commonName, clientName);
    }

    @Override
    public <T> PlatformProxy<T> platformProxy() {
        return new PlatformProxyImpl<>(platform().name());
    }

    @Override
    public <T> ModProxy<T> modProxy() {
        return new ModProxyImpl<>((modId) -> platform().getModInfo(modId));
    }

    @Override
    public <T> ModProxy<T> modProxy(Identifier identifier) {
        return new ModProxyImpl<>((modId) -> platform().getModInfo(modId), identifier);
    }

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }

        registerModule(new BalmRegistrars(this, "balm"), new InternalsModule());
        registerModule(new BalmRegistrars(this, "balm"), new ConfigSync());
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (platform().isModLoaded(modId)) {
            try {
                Class.forName(className).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                logger.error("Error initializing class {}", className, e);
            }
        }
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

    @Override
    public void poiTypes(String namespace, Consumer<BalmPoiTypeRegistrar> initializer) {
        initializer.accept(new BalmPoiTypeRegistrarImpl(registrar(), namespace));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent> BidirectionalEventMapper<Consumer<TEvent>> createBoundCustomEvent(Class<TEvent> eventClass) {
        final var nativeEventFactory = EventFactory.createArrayBacked(Consumer.class, (consumers) -> (rec) -> {
            for (final var consumer : consumers) {
                consumer.accept(rec);
            }
        });
        final var mapper = new EventMapperImpl<Consumer<TEvent>>(eventClass.getName());
        mapper.configureMapping(nativeEventFactory::register, nativeEventFactory::invoker);
        return mapper;
    }
}
