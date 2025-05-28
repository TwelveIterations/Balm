package net.blay09.mods.balm.api;

public enum BalmEnvironment {
    CLIENT,
    DEDICATED_SERVER;

    public boolean isClient() {
        return this == CLIENT;
    }
}
