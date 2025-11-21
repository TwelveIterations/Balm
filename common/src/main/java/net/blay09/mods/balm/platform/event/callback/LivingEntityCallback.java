package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityCallback {
    interface Heal {
        @FunctionalInterface
        interface Before {
            float computeHeal(LivingEntity entity, float healAmount);

            EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.Heal.Before");
        }
    }

    interface Fall {
        @FunctionalInterface
        interface Before {
            float computeFallDamage(LivingEntity entity, float fallDamage);

            EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.Fall.Before");
        }
    }

    interface Death {
        @FunctionalInterface
        interface Before {
            boolean allowDeath(LivingEntity entity, DamageSource damageSource);

            EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.Death.Before");
        }
    }

    interface Damage {
        @FunctionalInterface
        interface Before {
            float computeDamage(LivingEntity entity, DamageSource damageSource, float damageAmount);

            EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.Damage.Before");
        }
    }

}
