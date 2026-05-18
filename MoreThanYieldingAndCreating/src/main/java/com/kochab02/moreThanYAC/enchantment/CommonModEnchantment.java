package com.kochab02.moreThanYAC.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CommonModEnchantment extends Enchantment
{
    private int maxLevel;
    private boolean isCurse;
    private boolean hasDamageBonus;
    private float damageBonusIndex;

    public CommonModEnchantment(Rarity rarity,
                                EnchantmentCategory category,
                                EquipmentSlot[] equipmentSlots,
                                int maxLevel,
                                boolean isCurse,
                                boolean hasDamageBonus,
                                float damageBonusIndex){
        super(rarity,category,equipmentSlots);
        this.maxLevel = maxLevel;
        this.isCurse = isCurse;
        this.hasDamageBonus=hasDamageBonus;
        this.damageBonusIndex=damageBonusIndex;
    }

    @Override
    public int getMaxLevel() {return Math.max(1,maxLevel);}

    @Override
    public boolean isCurse(){return isCurse;}

    @Override
    public float getDamageBonus(int pLevel, MobType pMobType){
        return hasDamageBonus ? pLevel*damageBonusIndex+0.5f : 0.0f;
    }
}
