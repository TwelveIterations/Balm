package net.blay09.mods.balm.client.platform.runtime.internal;

import net.blay09.mods.balm.client.platform.BalmClientHooks;
import net.blay09.mods.balm.client.platform.internal.CommonBalmClientHooks;
import net.blay09.mods.balm.client.platform.module.internal.InternalsClientModule;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.platform.config.internal.ConfigSyncClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonBalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> implements BalmClientRuntime<TLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());

    private final BalmClientHooks clientHooks = new CommonBalmClientHooks();
    private boolean ready;

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
    public BalmClientHooks clientHooks() {
        return clientHooks;
    }

    @Override
    public void registerModule(BalmClientRegistrars registrars, BalmClientModule module) {
        modules.add(module);
        initializeModule(module);
    }

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }

        registerModule(new BalmClientRegistrars(this, "balm"), new ConfigSyncClient());
        registerModule(new BalmClientRegistrars(this, "balm"), new InternalsClientModule());
    }

}
