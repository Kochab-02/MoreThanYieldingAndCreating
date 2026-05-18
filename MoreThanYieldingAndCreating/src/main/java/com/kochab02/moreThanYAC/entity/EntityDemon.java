package com.kochab02.moreThanYAC.entity;

import com.kochab02.moreThanYAC.registries.ModItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
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

public class EntityDemon extends Monster
{
    public EntityDemon(EntityType<? extends Monster> type, Level level){
        super(type,level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH,30.0D)
                .add(Attributes.ARMOR,2)
                .add(Attributes.ATTACK_DAMAGE,2.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.9D)
                .add(Attributes.MOVEMENT_SPEED,0.1D)
                .add(Attributes.FOLLOW_RANGE,16.0D);
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this,2.0D,false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1,new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag){
        ItemStack item = new ItemStack(ModItemHandler.DEMON_SWORD.get());
        this.setItemSlot(EquipmentSlot.MAINHAND,item);
        this.setItemSlot(EquipmentSlot.OFFHAND,item);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0f);
        return spawnData;
    }

    @Override
    public boolean doHurtTarget(Entity target){
        if (this.level().isClientSide()) return false;
        float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        Difficulty difficulty = this.level().getDifficulty();

        float finalDamage = baseDamage;
        switch (difficulty){
            case PEACEFUL -> finalDamage = baseDamage*0;
            case EASY -> finalDamage = Math.min(baseDamage*0.5f+1f,baseDamage);
            case HARD -> finalDamage = baseDamage*1.5f;
        }
        return target.hurt(this.damageSources().mobAttack(this), finalDamage);
    }
}