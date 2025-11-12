package net.blay09.mods.balm.api.event;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.EventPhases} instead.
 */
@Deprecated
public enum EventPriority {
    Lowest,
    Low,
    Normal,
    High,
    Highest;

    public static EventPriority[] values = EventPriority.values();
}
