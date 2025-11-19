package net.blay09.mods.balm.platform;

public enum BalmEnvironment {
    CLIENT,
    DEDICATED_SERVER;

    public boolean isClient() {
        return this == CLIENT;
    }
}
