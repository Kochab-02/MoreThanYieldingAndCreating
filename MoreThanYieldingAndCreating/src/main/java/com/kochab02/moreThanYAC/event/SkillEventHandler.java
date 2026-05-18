package com.kochab02.moreThanYAC.event;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.utils.ModDamageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class SkillEventHandler
{
    @SubscribeEvent
    public static void onSlash(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide()) return;
        if (!entity.getTags().contains("onSlash")) return;

        CompoundTag tag = entity.getPersistentData();
        double startX = tag.getDouble("slashStartX");
        double startY = tag.getDouble("slashStartY");
        double startZ = tag.getDouble("slashStartZ");
        double maxDistance = 20.0;
        double maxTime = 200;
        int ticks = tag.getInt("slashTicks");
        ServerLevel level = (ServerLevel) entity.level();
        UUID targetID = tag.contains("slashTarget") ? tag.getUUID("slashTarget") : null;

        boolean shouldStop=false;
        boolean shouldAttack=false;
        boolean attackTarget= false;

        double dX = entity.getX();
        double dY = entity.getY();
        double dZ = entity.getZ();
        double distance;

        LivingEntity attackableEntity=null;

        distance=Math.sqrt(Math.pow(dX-startX,2)+Math.pow(dY-startY,2)+Math.pow(dZ-startZ,2));

        if(distance>=maxDistance){
            shouldStop = true;
        }

        if (!shouldStop) {
            AABB bb = entity.getBoundingBox().inflate(0.2);
            List<Entity> collidedEntities = level.getEntities(entity, bb).stream()
                    .filter(e -> e != entity)
                    .collect(Collectors.toList());

            if (!collidedEntities.isEmpty()) {
                // 记录撞到的第一个实体
                UUID hitEntityUUID = collidedEntities.get(0).getUUID();
                if (level.getEntity(hitEntityUUID) instanceof LivingEntity){
                    shouldStop = true;
                    shouldAttack = true;
                    attackableEntity = (LivingEntity) level.getEntity(hitEntityUUID);
                }
                if (hitEntityUUID.equals(targetID)){
                    attackTarget=true;
                }
            }
        }

        if (!shouldStop){
            Vec3 motion = entity.getDeltaMovement();
            boolean isMovingHorizontally = Math.abs(motion.x) > 0.01 || Math.abs(motion.z) > 0.01;

            int tick = tag.getInt("slashTicks");
            if (tick > 5 && isMovingHorizontally && (entity.horizontalCollision ||
                    (entity.verticalCollision && !entity.onGround()))) {
                shouldStop = true;
            }
        }

        if (ticks >= maxTime){
            shouldStop = true;
        }

        if(shouldStop){
            tag.remove("slashStartX");
            tag.remove("slashStartY");
            tag.remove("slashStartZ");
            tag.remove("slashTicks");
            tag.remove("slashTarget");
            tag.remove("slashVX");
            tag.remove("slashVY");
            tag.remove("slashVZ");
            entity.removeTag("onSlash");
            entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);

            if(shouldAttack){
                entity.playSound(SoundEvents.ENDER_DRAGON_GROWL);

                var holder = entity.level().registryAccess().
                        registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageType.DEMON_SLASH);
                DamageSource source = new DamageSource(holder,entity);

                if(attackTarget){
                    if (attackableEntity != null) {
                        attackableEntity.hurt(source, 12);
                    }
                }else{
                    if (attackableEntity != null) {
                        attackableEntity.hurt(source,6);
                    }
                }
            }else{
                entity.playSound(SoundEvents.ANVIL_DESTROY,1,1);
            }
        }else{
            tag.putInt("slashTicks", ticks + 1);
            entity.setDeltaMovement(tag.getDouble("slashVX"),tag.getDouble("slashVY"),tag.getDouble("slashVZ"));
        }
    }

    @SubscribeEvent
    public static void excludingAttack(LivingAttackEvent event){
        var source = event.getSource();
        if (!(source.getEntity() instanceof LivingEntity entity)) return;
        if (source.is(ModDamageType.DEMON_SLASH)) return;
        if (entity.level().isClientSide()) return;
        if (entity.getTags().contains("onSlash")) {
            event.setCanceled(true);
        }
    }
}
