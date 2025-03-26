package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.LivingEntity;

public class LivingHealEvent extends BalmEvent {
    private final LivingEntity entity;
    private final float amount;

    public LivingHealEvent(LivingEntity entity, float amount) {
        this.entity = entity;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public float getAmount() {
        return amount;
    }

}
