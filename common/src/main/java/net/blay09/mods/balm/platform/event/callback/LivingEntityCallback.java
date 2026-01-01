package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

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

    interface MobEffectCallback {
        interface Apply {
            @FunctionalInterface
            interface Before {
                boolean allowApply(LivingEntity entity, MobEffectInstance effectInstance, @Nullable Entity source);

                EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.MobEffectCallback.Apply.Before");
            }
        }

        interface Add {
            @FunctionalInterface
            interface Before {
                void effectAdded(LivingEntity entity, MobEffectInstance effectInstance, @Nullable MobEffectInstance previousEffectInstance, @Nullable Entity source);

                EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.MobEffectCallback.Add.Before");
            }
        }

        interface Remove {
            @FunctionalInterface
            interface Before {
                boolean allowRemove(LivingEntity entity, Holder<MobEffect> effect, @Nullable MobEffectInstance effectInstance);

                EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.MobEffectCallback.Remove.Before");
            }
        }

        interface Expire {
            @FunctionalInterface
            interface Before {
                boolean allowExpire(LivingEntity entity, @Nullable MobEffectInstance effectInstance);

                EventMapper<Before> EVENT = EventMapper.createUnbound("LivingEntityCallback.MobEffectCallback.Expire.Before");
            }
        }
    }

}
