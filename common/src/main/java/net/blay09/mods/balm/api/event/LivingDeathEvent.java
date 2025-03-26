package net.blay09.mods.balm.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingDeathEvent extends BalmEvent {
    private final LivingEntity entity;
    private final DamageSource damageSource;

    public LivingDeathEvent(LivingEntity entity, DamageSource damageSource) {
        this.entity = entity;
        this.damageSource = damageSource;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }
}
