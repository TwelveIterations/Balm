package net.blay09.mods.balm.api;

public enum BalmEnvironment {
    CLIENT,
    SERVER;

    public boolean isClient() {
        return this == CLIENT;
    }
}
