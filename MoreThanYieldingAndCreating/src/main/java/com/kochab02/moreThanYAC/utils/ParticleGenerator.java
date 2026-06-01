package com.kochab02.moreThanYAC.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;


import java.util.ArrayList;
import java.util.Random;

public class ParticleGenerator
{
    public static void generateBurstParticles(LivingEntity entity,
                                              ParticleOptions particleType,
                                              double speed, int countPerDir, double angleSpread) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;
        Random RANDOM = new Random();
        double x = entity.getX();
        double y = entity.getY() + 0.1;
        double z = entity.getZ();

        // 八个水平方向（弧度）
        double[] azimuths = {0, Math.PI/4, Math.PI/2, 3*Math.PI/4,
                Math.PI, 5*Math.PI/4, 3*Math.PI/2, 7*Math.PI/4};

        // 三个仰角（弧度）
        double[] elevations = {Math.toRadians(15), Math.toRadians(45), Math.toRadians(75)};

        for (double azimuth : azimuths) {
            for (double elevation : elevations) {
                for (int i = 0; i < countPerDir; i++) {
                    // 给角度添加随机偏移
                    double azOffset = (RANDOM.nextDouble() - 0.5) * angleSpread;
                    double elOffset = (RANDOM.nextDouble() - 0.5) * angleSpread;
                    double finalAz = azimuth + azOffset;
                    double finalEl = elevation + elOffset;

                    double cosAz = Math.cos(finalAz);
                    double sinAz = Math.sin(finalAz);
                    double cosEl = Math.cos(finalEl);
                    double sinEl = Math.sin(finalEl);

                    // 速度分量（可再添加随机速度波动）
                    double vx = speed * cosEl * cosAz;
                    double vz = speed * cosEl * sinAz;
                    double vy = speed * sinEl;

                    serverLevel.sendParticles(particleType, x, y, z, 1, vx, vy, vz, 0.0);
                }
            }
        }
    }
    public static void generateComingInParticles(LivingEntity entity,
                                                 ParticleOptions particleType,
                                                 double speed, int count,double radius){
        Level level = entity.level();
        Random random = new Random();
        double x = entity.getX();
        double y = entity.getY() + 0.1;
        double z = entity.getZ();
        //.
        double xis,zis,vxis,vzis;
        for (int index=0;index<count;index++){
            double currentAngle = (index * 2 * Math.PI) / count;
            xis = radius*Math.cos(currentAngle)+ random.nextDouble()*0.2d +x;
            zis = radius*Math.sin(currentAngle)+ random.nextDouble()*0.2d +z;

            vxis = speed*Math.cos(currentAngle)+ MathUtils.sgn(Math.cos(currentAngle))*random.nextDouble()*0.1d;
            vzis = speed*Math.sin(currentAngle)+ MathUtils.sgn(Math.sin(currentAngle))*random.nextDouble()*0.1d;

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(particleType, xis, y, zis, 0, -vxis, 0, -vzis,1.0);
                serverLevel.sendParticles(particleType, xis, y, zis, 1, -vxis, 0, -vzis,0.0);
            } else {
                level.addParticle(particleType, xis, y, zis, -vxis, 0, -vzis);
            }
        }
    }

}
