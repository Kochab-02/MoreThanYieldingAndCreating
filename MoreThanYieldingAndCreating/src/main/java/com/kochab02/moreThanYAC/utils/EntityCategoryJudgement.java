package com.kochab02.moreThanYAC.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;

public class EntityCategoryJudgement
{
    public static boolean isFriendly(LivingEntity entity){
        if (entity instanceof Animal) return true;
        if (entity instanceof Villager) return true;
        return entity instanceof AmbientCreature;
    }

    public static boolean isNeutral()
    {
        return false;
    }

    public static boolean isHostile(LivingEntity entity){
        if (entity instanceof Monster) return true;
        return entity instanceof Enemy;
    }
}
