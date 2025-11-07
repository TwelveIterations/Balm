package net.blay09.mods.balm.api.event;

@Deprecated
public abstract class BalmEvent {
    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
