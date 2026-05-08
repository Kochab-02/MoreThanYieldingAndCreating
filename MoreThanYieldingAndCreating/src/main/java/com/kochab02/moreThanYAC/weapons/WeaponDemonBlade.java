package com.kochab02.moreThanYAC.weapons;

import com.kochab02.moreThanYAC.utils.ModToolTiers;
import net.minecraft.world.item.SwordItem;

public class WeaponDemonBlade extends SwordItem
{
    public WeaponDemonBlade(ModToolTiers tier, int attackDamageModifier, float attackSpeedModifier, Properties properties){
        super(tier,attackDamageModifier,attackSpeedModifier,properties);
    }
}
