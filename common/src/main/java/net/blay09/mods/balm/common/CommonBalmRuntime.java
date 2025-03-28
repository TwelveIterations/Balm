package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.proxy.SidedProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonBalmRuntime<TLoadContext extends BalmRuntimeLoadContext> implements BalmRuntime<TLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final SidedProxy<BalmProxy> proxy = sidedProxy("net.blay09.mods.balm.api.BalmProxy", "net.blay09.mods.balm.api.client.BalmClientProxy");

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

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }

        registerModule(new BaseModule());
    }

}
