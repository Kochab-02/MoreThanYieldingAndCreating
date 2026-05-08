package com.name.moreThanYAC.utils;

import com.name.moreThanYAC.enchantment.CommonModEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantmentBuilder
{
    private Rarity rarity;
    private EnchantmentCategory category;
    private int maxLevel;
    private boolean isCurse=false;
    private List<EquipmentSlot> slots = new ArrayList<>();
    private boolean hasDamageBonus=false;
    private float damageBonusIndex=0f;

    public ModEnchantmentBuilder rarity(Rarity rarity){this.rarity=rarity; return this;}
    public ModEnchantmentBuilder category(EnchantmentCategory category){this.category=category; return this;}
    public ModEnchantmentBuilder addCompatibleSlot(EquipmentSlot slot){slots.add(slot); return this;}
    public ModEnchantmentBuilder setMaxLevel(int level){this.maxLevel=level; return this;}
    public ModEnchantmentBuilder isCurse(){this.isCurse=true; return this;}
    public ModEnchantmentBuilder hasDamageBonus(){this.hasDamageBonus=true; return this;}
    public ModEnchantmentBuilder damageBonusIndex(float index){this.damageBonusIndex=index; return this;}


    public CommonModEnchantment build(){
        return new CommonModEnchantment(rarity,category,slots.toArray(new EquipmentSlot[0]),
                maxLevel,isCurse,hasDamageBonus,damageBonusIndex);
    }
}
