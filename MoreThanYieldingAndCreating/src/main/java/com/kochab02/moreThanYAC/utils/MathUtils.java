package com.kochab02.moreThanYAC.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class MathUtils
{
    public static int sgn(int a){
        return Integer.compare(a, 0);
    }
    public static float sgn(float a){
        return Float.compare(a, 0);
    }
    public static double sgn(double a){
        return Double.compare(a, 0);
    }

    public static double distanceTo(LivingEntity entity, BlockPos pos){
        double x1 = entity.getX();
        double x = pos.getX();
        double y1 = entity.getY();
        double y = pos.getY();
        double z1 = entity.getZ();
        double z = pos.getZ();

        return Math.sqrt(Math.pow(x1-x,2)+Math.pow(y1-y,2)+Math.pow(z1-z,2));
    }
}
