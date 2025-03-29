package net.blay09.mods.balm.api;

public class EmptyLoadContext implements BalmRuntimeLoadContext {
    public static final EmptyLoadContext INSTANCE = new EmptyLoadContext();

    private EmptyLoadContext() {
    }
}
