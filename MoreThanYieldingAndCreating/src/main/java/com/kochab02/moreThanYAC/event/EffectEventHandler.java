package com.kochab02.moreThanYAC.event;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.entity.EntityCorruptedDemon;
import com.kochab02.moreThanYAC.entity.EntityDemon;
import com.kochab02.moreThanYAC.registries.ModEffectHandler;
import com.kochab02.moreThanYAC.registries.ModEntityHandler;
import com.kochab02.moreThanYAC.utils.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.UUID;

import static com.kochab02.moreThanYAC.entity.skills.SlashSkillTriggerer.triggerSlash;
import static com.kochab02.moreThanYAC.entity.skills.SurgeSkillTriggerer.triggerSurge;
import static com.kochab02.moreThanYAC.utils.EntityCategoryJudgement.isFriendly;
import static com.kochab02.moreThanYAC.utils.ModUsefulFunctions.calculateCanGenerateBlockPos;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandler
{
    @SubscribeEvent
    public static void onPlayerKillingYoung(LivingDeathEvent event){
        if (!ModConfig.ENABLE_SANGJINTIANLIANG.get()) return;
        var source = event.getSource();
        if (!(source.getEntity() instanceof Player attacker)) return;
        LivingEntity entity = event.getEntity();
        if (isFriendly(entity)){
            if (entity.isBaby()) {
                if (attacker.level().isClientSide()) return;
                attacker.addEffect(new MobEffectInstance(ModEffectHandler.SANGJINTIANLIANG.get(),
                        6000, 0, false, true));
            }
        }
    }
    @SubscribeEvent
    public static void onSangJinRemove(MobEffectEvent.Remove event){
        if (!ModConfig.ENABLE_SANGJINTIANLIANG.get()) return;
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(victim.level() instanceof ServerLevel level)) return;
        MobEffectInstance instance = event.getEffectInstance();

        if (instance !=null && event.getEffect() == ModEffectHandler.SANGJINTIANLIANG.get()){
            victim.removeTag("SANGTime");
            summonDemonTeam(victim.getOnPos().above(),level, Objects.requireNonNull(victim
                    .getEffect(ModEffectHandler.SANGJINTIANLIANG.get())).getAmplifier());
        }
    }
    @SubscribeEvent
    public static void onSangJinExpire(MobEffectEvent.Expired event){
        if (!ModConfig.ENABLE_SANGJINTIANLIANG.get()) return;
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(victim.level() instanceof ServerLevel level)) return;
        MobEffectInstance instance = event.getEffectInstance();

        if (instance !=null && instance.getEffect() == ModEffectHandler.SANGJINTIANLIANG.get()){
            victim.removeTag("SANGTime");
            BlockPos pos = new BlockPos(victim.getOnPos().above().getX(),
                    victim.getOnPos().above().getY(),victim.getOnPos().above().getZ()+5);
            BlockPos pos1 = calculateCanGenerateBlockPos(level,pos,5,2);
            summonDoubleDemon(pos1,level);
        }
    }

    //below are functions that are essential to demon summoning.

    private static void summonDemonTeam(BlockPos playerPos,ServerLevel level,int amplifier){
        BlockPos posD = new BlockPos(playerPos.getX(),playerPos.getY(),playerPos.getZ()+5);
        BlockPos posC1 = new BlockPos(playerPos.getX()+1,playerPos.getY(),playerPos.getZ()+5);
        BlockPos posC2 = new BlockPos(playerPos.getX()-1,playerPos.getY(),playerPos.getZ()+5);
        BlockPos pos1 = calculateCanGenerateBlockPos(level,posD,5,2);
        BlockPos pos2 = calculateCanGenerateBlockPos(level,posC1,0, 2);
        BlockPos pos3 = calculateCanGenerateBlockPos(level,posC2,0,2);
        for (int i=0;i<=amplifier;i++){
            summonDoubleDemon(pos1,level);
            summonDemon(pos2, level);
            summonCorruptedDemon(pos3, level);
        }
    }
    private static void summonDemon(BlockPos pos,ServerLevel level){
        EntityDemon demon = ModEntityHandler.ENTITY_DEMON
                .get().spawn(level,pos.above(),MobSpawnType.TRIGGERED);
        if (demon != null) demon.setPersistenceRequired();
    }
    private static void summonCorruptedDemon(BlockPos pos,ServerLevel level){
        EntityCorruptedDemon demon = ModEntityHandler.ENTITY_CORRUPTED_DEMON
                .get().spawn(level,pos.above(),MobSpawnType.TRIGGERED);
        if (demon != null) demon.setPersistenceRequired();
    }
    private static void summonDoubleDemon(BlockPos pos,ServerLevel level){
        EntityDemon demon1 = ModEntityHandler.ENTITY_DEMON
                .get().spawn(level,pos.above(),MobSpawnType.TRIGGERED);

        EntityDemon demon2 = ModEntityHandler.ENTITY_DEMON
                .get().spawn(level,pos.above(),MobSpawnType.TRIGGERED);
        if (demon1 != null && demon2 != null) {
            demon1.setPersistenceRequired();
            demon2.setPersistenceRequired();
            UUID uuid1 = demon1.getUUID();
            UUID uuid2 = demon2.getUUID();
            CompoundTag tag1 = demon1.getPersistentData();
            CompoundTag tag2 = demon2.getPersistentData();
            tag1.putUUID("twinUUID",uuid2);
            tag2.putUUID("twinUUID",uuid1);
        }
    }
    @SubscribeEvent
    public static void onTwinDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof EntityDemon)) return;
        if (entity.level().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof Player attacker)) return;
        ServerLevel level = (ServerLevel) entity.level();
        CompoundTag origTag = entity.getPersistentData();
        if(origTag.contains("twinUUID")){
            UUID twinID = origTag.getUUID("twinUUID");
            origTag.remove("twinUUID");
            Entity entity1 = level.getEntity(twinID);
            if(entity1!=null){
                if(entity1 instanceof EntityDemon entity2){
                    CompoundTag victimTag = entity1.getPersistentData();
                    if(victimTag.contains("twinUUID")){
                        victimTag.remove("twinUUID");
                    }
                    triggerSurge(entity2);
                    triggerSlash(entity2,attacker);
                    entity2.addTag("Surge");
                }
            }
        }
    }

    //below are functions that prevent player from removing the effect through death
    @SubscribeEvent
    public static void detectPlayerSangJinEffect(LivingDeathEvent event){
        if (!ModConfig.ENABLE_SANGJINTIANLIANG.get()) return;
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;
        CompoundTag tag = player.getPersistentData();
        if (player.level().isClientSide()) return;
        if (player.getEffect(ModEffectHandler.SANGJINTIANLIANG.get()) != null) {
            int time = Objects.requireNonNull(player.getEffect(ModEffectHandler.SANGJINTIANLIANG.get())).getDuration();
            if (time > 0) {
                tag.putInt("SANGTime", time);
                System.out.println("[debug] stored remaining time.");
            } else {
                tag.remove("SANGTime");
            }
        }
    }
    @SubscribeEvent
    public static void onReAddingSang(PlayerEvent.Clone event){
        if (!ModConfig.ENABLE_SANGJINTIANLIANG.get()) return;
        if (!event.isWasDeath()) return;
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        CompoundTag oldTag = oldPlayer.getPersistentData();
        if (newPlayer.level().isClientSide()) return;
        if (oldTag.contains("SANGTime")){
            int duration = oldTag.getInt("SANGTime");
            System.out.println("[Debug] readded sang,duration is"+duration);
            if (duration > 0) {
                newPlayer.addEffect(new MobEffectInstance(ModEffectHandler.SANGJINTIANLIANG.get(),
                        duration, 0, false, true));
                oldTag.remove("SANGTime");
            }
        }
    }
}
