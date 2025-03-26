package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityAddedEvent extends BalmEvent {
    private final Entity entity;
    private final Level level;

    public EntityAddedEvent(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }

    public Entity getEntity() {
        return entity;
    }

    public Level getLevel() {
        return level;
    }
}
