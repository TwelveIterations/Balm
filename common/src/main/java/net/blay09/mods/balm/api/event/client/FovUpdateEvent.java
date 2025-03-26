package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.entity.LivingEntity;

public class FovUpdateEvent extends BalmEvent {
    private final LivingEntity entity;
    private final float originalFov;
    private Float fov;

    public FovUpdateEvent(LivingEntity entity, float originalFov) {
        this.entity = entity;
        this.originalFov = originalFov;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public float getOriginalFov() {
        return originalFov;
    }

    public Float getFov() {
        return fov;
    }

    public void setFov(Float fov) {
        this.fov = fov;
    }
}
