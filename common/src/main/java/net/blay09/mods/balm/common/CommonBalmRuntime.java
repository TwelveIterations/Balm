package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.common.proxy.ModProxyImpl;
import net.blay09.mods.balm.common.proxy.PlatformProxyImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return new SidedProxy<>(this::getEnvironment, commonName, clientName);
    }

    @Override
    public <T> PlatformProxy<T> platformProxy() {
        return new PlatformProxyImpl<>(getPlatform());
    }

    @Override
    public <T> ModProxy<T> modProxy() {
        return new ModProxyImpl<>(this::isModLoaded);
    }

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }

        registerModule(new BaseModule());
    }

}
