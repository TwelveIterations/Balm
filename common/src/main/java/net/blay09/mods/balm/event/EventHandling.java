package net.blay09.mods.balm.event;

public enum EventHandling {
    RESUME,
    SKIP_DEFAULT,
    CANCEL;

    public EventHandling merge(EventHandling next) {
        return switch (this) {
            case RESUME -> next;
            case SKIP_DEFAULT -> next == CANCEL ? CANCEL : SKIP_DEFAULT;
            case CANCEL -> CANCEL;
        };
    }

    public boolean shouldSkipListeners() {
        return this == CANCEL;
    }

    public boolean shouldSkipDefault() {
        return this == SKIP_DEFAULT || this == CANCEL;
    }
}
