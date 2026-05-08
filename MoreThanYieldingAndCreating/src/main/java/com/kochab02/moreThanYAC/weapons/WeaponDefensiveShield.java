package com.name.moreThanYAC.weapons;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;

public class WeaponDefensiveShield extends ShieldItem
{
    public WeaponDefensiveShield(){
        super(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).durability(500));
    }
}
