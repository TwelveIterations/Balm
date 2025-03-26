package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.entity.LivingEntity;

public class FovUpdateEvent extends BalmEvent {
    private final LivingEntity entity;
    private Float fov;

    public FovUpdateEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Float getFov() {
        return fov;
    }

    public void setFov(Float fov) {
        this.fov = fov;
    }
}
