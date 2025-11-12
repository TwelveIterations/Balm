package net.blay09.mods.balm.api.event;

import net.minecraft.world.level.LevelAccessor;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback} instead.
 */
@Deprecated
public abstract class LevelLoadingEvent {
    private final LevelAccessor level;

    public LevelLoadingEvent(LevelAccessor level) {
        this.level = level;
    }

    public LevelAccessor getLevel() {
        return level;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback#LOAD} instead.
     */
    @Deprecated
    public static class Load extends LevelLoadingEvent {
        public Load(LevelAccessor level) {
            super(level);
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback#UNLOAD} instead.
     */
    @Deprecated
    public static class Unload extends LevelLoadingEvent {
        public Unload(LevelAccessor level) {
            super(level);
        }
    }

}
