package com.name.moreThanYAC.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class FunctionCalculator
{
    //this function combines crit and advantage/disadvantage
    public static float onGeneralHurt(float origDamage,LivingEntity victim,Entity entity){
        float damageIndex=1.0f;
        if (victim.level().isClientSide()) return origDamage;
        if (!(entity instanceof LivingEntity attacker)) return origDamage;
        if (!attacker.onGround()) return origDamage;
        Random random = new Random(System.currentTimeMillis());
        if (ModConfig.ENABLE_GENERAL_CRITICAL.get()){
            if (random.nextInt(10)==0){
                damageIndex*=1.5f;
                ParticleGenerator.generateBurstParticles(victim,ParticleTypes.ELECTRIC_SPARK,0.8,5,0.5);
            }
        }
        if (ModConfig.ENABLE_HIGH_GROUND_ADVANTAGE.get()){
            float attackerY = (float) attacker.getY();
            float victimY = (float) victim.getY();
            if (attackerY>victimY){
                damageIndex*=ModConfig.HIGH_GROUND_ADVANTAGE_DAMAGE_INDEX.get();
                ParticleGenerator.generateBurstParticles(victim,ParticleTypes.END_ROD,0.8,5,0.5);
            } else if (attackerY<victimY){
                int index = random.nextInt(10);
                switch (index) {
                    case 0:
                        damageIndex*=0;
                        break;
                    case 1:
                    case 2:
                    case 3:
                        damageIndex*=ModConfig.LOWER_GROUND_DISADV_DAMAGE_INDEX.get();
                        break;
                    default:
                        break;
                }
            }
        }
        return origDamage * damageIndex;
    }
}
