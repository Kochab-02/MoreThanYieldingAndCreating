package com.name.moreThanYAC.entity.skills;

import com.name.moreThanYAC.MoreThanYAC;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.name.moreThanYAC.utils.ParticleGenerator.generateBurstParticles;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class SlashSkillTriggerer
{
    private static LivingEntity target = null;
    private static LivingEntity attacker = null;
    private static int delayedTick =0;
    private static Runnable delayedTask = null;

    public static void triggerSlash(LivingEntity entity,LivingEntity victim){
        if (entity.level().isClientSide()) return;
        target=victim;
        attacker=entity;
        if (target != null && target.isAlive() && !target.isRemoved()) {
            if (attacker != null && attacker.isAlive() && !attacker.isRemoved()){
                attacker.lookAt(EntityAnchorArgument.Anchor.EYES,target.getEyePosition());
            }
        }
        delayedTick=60;

        delayedTask = () -> {
            attacker.lookAt(EntityAnchorArgument.Anchor.EYES,target.getEyePosition());
            generateBurstParticles(attacker, ParticleTypes.ASH,1.0,10,0.3);
            // 2. 计算从 entity 指向 target 的方向向量
            double dx = target.getX() - attacker.getX();
            double dz = target.getZ() - attacker.getZ();
            double dy = target.getY() - attacker.getY();

            // 水平距离
            double horizDist = Math.sqrt(dx * dx + dz * dz);
            if (horizDist < 0.001) return;

            // 归一化方向（只考虑水平方向，也可以保留垂直）
            double vx = dx / horizDist;
            double vz = dz / horizDist;
            double vy = dy / horizDist; // 如果需要垂直移动，否则设为0

            // 3. 应用速度（保留原有垂直速度）
            double slashVY = attacker.getDeltaMovement().y + vy * 0.5; // 可选
            double slashVX = vx*0.5;
            double slashVZ = vz*0.5;
            attacker.setDeltaMovement(slashVX,slashVY,slashVZ);
            attacker.addTag("onSlash");
            CompoundTag tag = attacker.getPersistentData();
            tag.putDouble("slashStartX",attacker.getX());
            tag.putDouble("slashStartY",attacker.getY());
            tag.putDouble("slashStartZ",attacker.getZ());
            tag.putDouble("slashVX",slashVX);
            tag.putDouble("slashVY",slashVY);
            tag.putDouble("slashVZ",slashVZ);
            tag.putInt("slashTicks",0);
            tag.putUUID("slashTarget",target.getUUID());
        };
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (delayedTick > 0) {
            delayedTick--;
            if (delayedTick == 0 && delayedTask != null) {
                delayedTask.run();
            }
        }
    }
}
