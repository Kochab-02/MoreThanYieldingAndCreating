package com.name.moreThanYAC.utils;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum ModToolTiers implements Tier
{
    DEMON(4,2501,12.0f,4.0f,15,() -> Ingredient.of(Items.IRON_INGOT));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final java.util.function.Supplier<Ingredient> repairIngredient;

    ModToolTiers(int level, int uses, float speed, float damage,
                 int enchantmentValue, java.util.function.Supplier<Ingredient> repairIngredient){
        this.level=level;
        this.uses=uses;
        this.speed=speed;
        this.damage=damage;
        this.enchantmentValue=enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getLevel() { return level; }
    @Override public int getUses() { return uses; }
    @Override public float getSpeed() { return speed; }
    @Override public float getAttackDamageBonus() { return damage; }
    @Override public int getEnchantmentValue() { return enchantmentValue; }
    @Override public @NotNull Ingredient getRepairIngredient() { return repairIngredient.get(); }
}
