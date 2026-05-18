package com.kochab02.moreThanYAC.entity;

import com.kochab02.moreThanYAC.registries.ModItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class EntityCorruptedDemon extends Monster
{
    public EntityCorruptedDemon(EntityType<? extends Monster> type, Level level){
        super(type,level);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH,15.0f)
                .add(Attributes.ATTACK_DAMAGE,4.0D)
                .add(Attributes.ARMOR,0)
                .add(Attributes.FOLLOW_RANGE,64.0F)
                .add(Attributes.MOVEMENT_SPEED,0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.1D);
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1,new FloatGoal(this));
        this.goalSelector.addGoal(2,new MeleeAttackGoal(this,1.5D,true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.4D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 128.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1,new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));
        //this.targetSelector.addGoal(3,new NearestAttackableTargetGoal<>(this,EntityDemon.class,true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag){
        ItemStack item = new ItemStack(ModItemHandler.DEMON_BLADE.get());
        this.setItemSlot(EquipmentSlot.MAINHAND,item);
        this.setItemSlot(EquipmentSlot.OFFHAND,item);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0f);
        return spawnData;
    }
}
