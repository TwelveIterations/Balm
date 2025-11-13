package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventHandling;
import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityCallback {
    @FunctionalInterface
    interface Heal {
        float handle(LivingEntity entity, float amount);

        EventMapper<Heal> EVENT = EventMapper.createUnbound("LivingEntityCallback.Heal");
    }

    @FunctionalInterface
    interface Fall {
        float handle(LivingEntity entity, float fallDamage);

        EventMapper<Fall> EVENT = EventMapper.createUnbound("LivingEntityCallback.Fall");
    }

    @FunctionalInterface
    interface Death {
        EventHandling handle(LivingEntity entity, DamageSource damageSource);

        EventMapper<Death> PRE = EventMapper.createUnbound("LivingEntityCallback.Death.PRE");
        EventMapper<Death> POST = EventMapper.createUnbound("LivingEntityCallback.Death.POST");
    }

    @FunctionalInterface
    interface Damage {
        float handle(LivingEntity entity, DamageSource damageSource, float damageAmount);

        EventMapper<Damage> EVENT = EventMapper.createUnbound("LivingEntityCallback.Damage");
    }

}
