package com.kochab02.moreThanYAC.entity.skills;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.utils.ModConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kochab02.moreThanYAC.utils.ParticleGenerator.generateBurstParticles;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class SurgeSkillTriggerer
{
    private static int delayTicks = 0;
    private static Runnable delayedTask = null;
    private static LivingEntity targetEntity = null;

    public static void triggerSurge(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        generateBurstParticles(entity, ParticleTypes.FLAME, 1.0, 5, 0.3);
        entity.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 127, true, true));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 4, true, true));
        entity.setInvulnerable(true);
        entity.addTag("Surge");
        CompoundTag tag = entity.getPersistentData();
        tag.putInt("surgeTick",0);

        delayTicks = 60;
        targetEntity = entity;
        delayedTask = () -> {
            if (targetEntity != null && targetEntity.isAlive() && !targetEntity.isRemoved()) {
                targetEntity.setInvulnerable(false);
                targetEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200000, 1, true, false));
                targetEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200000, 0, true, false));
                targetEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200000, 0, true, false));

                targetEntity.playSound(SoundEvents.FIRECHARGE_USE, 5, 1);

                generateBurstParticles(targetEntity,
                        ParticleTypes.SOUL_FIRE_FLAME, 1.0, 5, 0.3);
            }
            // 清理
            targetEntity = null;
            delayedTask = null;
        };
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (delayTicks > 0) {
            delayTicks--;
            if (delayTicks == 0 && delayedTask != null) {
                delayedTask.run();
            }
        }
    }

    @SubscribeEvent
    public static void onSurgeTick(LivingEvent.LivingTickEvent event){
        if (!ModConfig.ENABLE_SURGE_PARTICLES.get()) return;
        LivingEntity entity = event.getEntity();
        if (entity.getTags().contains("Surge")){
            if (!entity.level().isClientSide()){
                CompoundTag tag = entity.getPersistentData();
                int tick = tag.getInt("surgeTick");
                if (tick==0){
                    generateBurstParticles(entity,
                            ParticleTypes.SOUL_FIRE_FLAME, 1.0, 5, 0.3);
                    tag.putInt("surgeTick",tick+1);
                } else if (tick<40) tag.putInt("surgeTick",tick+1);
                else if (tick==40) tag.putInt("surgeTick",0);
            }
        }
    }
}