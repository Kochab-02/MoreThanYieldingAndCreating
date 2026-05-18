package com.kochab02.moreThanYAC.registries;

import com.kochab02.moreThanYAC.MoreThanYAC;

import com.kochab02.moreThanYAC.utils.ModToolTiers;
import com.kochab02.moreThanYAC.weapons.WeaponDefensiveShield;
import com.kochab02.moreThanYAC.weapons.WeaponDemonBlade;
import com.kochab02.moreThanYAC.weapons.WeaponDemonSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemHandler
{
    public static final DeferredRegister<Item> MODITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MoreThanYAC.MODID);

    public static final RegistryObject<Item> DEMON_SWORD = MODITEMS.register("demon_sword",
            () -> new WeaponDemonSword(ModToolTiers.DEMON,
                    0,-2f,new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DEMON_BLADE = MODITEMS.register("demon_blade",
            () -> new WeaponDemonBlade(ModToolTiers.DEMON,2,-1.5f,
                    new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DEFENSIVE_SHIELD = MODITEMS.register("defensive_shield",
            WeaponDefensiveShield::new);

    public static final RegistryObject<Item> ITEM_WILL = MODITEMS.register("item_will",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ITEM_HOPE = MODITEMS.register("item_hope",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ITEM_DEMON_SCRAP = MODITEMS.register("item_demon_scrap",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));
}
