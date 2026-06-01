package com.kochab02.moreThanYAC.entity.skills;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.entity.EntityCorruptedDemon;
import com.kochab02.moreThanYAC.utils.ModConfig;
import com.kochab02.moreThanYAC.utils.ParticleGenerator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class DemonPowerSkill {
    private static final String canActivateTag = "mtyac_can_activate_power";
    private static int getCooldownTicks() {
        return 600;/*ModConfig.DEMON_POWER_COOLDOWN.get() * 20;*/
    }

    private static final Map<UUID, DelayData> delayMap = new HashMap<>();

    private static class DelayData {
        Runnable delayedTask1;
        Runnable delayedTask2;
        int delayedTick_untilFirstRun;
        int delayedTick_secondRun;
        boolean delayedFirstRun;
    }

    public static void triggerPowerSkill(LivingEntity demon, LivingEntity target) {
        if (!(demon instanceof EntityCorruptedDemon)) return;
        CompoundTag tag = demon.getPersistentData();
        if (!tag.getBoolean(canActivateTag)) return;

        tag.putBoolean(canActivateTag, false);
        tag.putInt("mtyac_power_cooldown_tag", getCooldownTicks());

        ParticleGenerator.generateComingInParticles(demon, ParticleTypes.FLAME, 0.8d, 30, 10.0);
        demon.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200,255));
        demon.playSound(SoundEvents.ENDER_DRAGON_GROWL,1,1);

        DelayData data = new DelayData();
        data.delayedTick_untilFirstRun = 60;
        data.delayedTick_secondRun = 100;
        data.delayedFirstRun = false;

        data.delayedTask1 = () -> {
            ParticleGenerator.generateComingInParticles(demon, ParticleTypes.FLAME, 0.8d, 30, 10.0);
            demon.playSound(SoundEvents.FIRECHARGE_USE,1,1);
        };
        data.delayedTask2 = () -> {
            ParticleGenerator.generateComingInParticles(target, ParticleTypes.SOUL_FIRE_FLAME, 0.6d, 40, 4.0);
        };

        delayMap.put(demon.getUUID(), data);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Map.Entry<UUID, DelayData>> it = delayMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, DelayData> entry = it.next();
            DelayData data = entry.getValue();

            if (!data.delayedFirstRun) {
                data.delayedTick_untilFirstRun--;
                if (data.delayedTick_untilFirstRun <= 0) {
                    if (data.delayedTask1 != null) {
                        data.delayedTask1.run();
                    }
                    data.delayedFirstRun = true;
                }
            } else {
                data.delayedTick_secondRun--;
                if (data.delayedTick_secondRun <= 0) {
                    if (data.delayedTask2 != null) {
                        data.delayedTask2.run();
                    }
                    it.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDemonTryingPowerSkill(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getEntity().level().isClientSide) return;
        if (!(entity instanceof EntityCorruptedDemon demon)) return;

        CompoundTag tag = entity.getPersistentData();
        String cooldownTag = "mtyac_power_cooldown_tag";

        int cd = tag.contains(cooldownTag) ? tag.getInt(cooldownTag) : -1;
        if (!tag.contains(cooldownTag)) {
            tag.putInt(cooldownTag, getCooldownTicks());
            tag.putBoolean(canActivateTag, false);
            return;
        }

        //int cd = tag.getInt(cooldownTag);
        if (cd > 0) {
            tag.putInt(cooldownTag, cd - 1);
            tag.putBoolean(canActivateTag, false);
            return;
        } else if (cd == 0) {
            tag.putBoolean(canActivateTag, true);
        }

        if (tag.getBoolean(canActivateTag)) {
            //if (entity.getRandom().nextInt(20) > 0) return;
            LivingEntity target = demon.getTarget();
            if (target == null) {
                target = demon.level().getNearestPlayer(demon, 20.0);
                if (target == null) {
                    return;
                }
                System.out.println("[Debug] Found nearby player: " + target.getName().getString());
            }
            triggerPowerSkill(demon, target);
            System.out.println("power activated");
        }
    }
}