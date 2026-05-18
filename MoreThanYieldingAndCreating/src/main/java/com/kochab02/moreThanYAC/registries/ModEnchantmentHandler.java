package com.kochab02.moreThanYAC.registries;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.utils.ModEnchantmentBuilder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEnchantmentHandler
{
    public static final DeferredRegister<Enchantment> MODENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MoreThanYAC.MODID);
    public static final EnchantmentCategory ATTACKABLE_ITEM = EnchantmentCategory.create("weapon_and_axe",
            item -> item instanceof SwordItem || item instanceof AxeItem);
    public static final EnchantmentCategory SHIELD_ITEM = EnchantmentCategory.create("shieldItem",
            item -> item instanceof ShieldItem);

    public static final RegistryObject<Enchantment> ENCHANT_DECISIVE_STRIKE =
            MODENCHANTMENTS.register("decisive_strike", () -> new ModEnchantmentBuilder()
                    .rarity(Rarity.RARE).category(ModEnchantmentHandler.ATTACKABLE_ITEM)
                    .addCompatibleSlot(EquipmentSlot.MAINHAND)
                    .setMaxLevel(4).hasDamageBonus().damageBonusIndex(0.5f).build());
    public static final RegistryObject<Enchantment> ENCHANT_BACK_TO_SQUARE_ONE =
            MODENCHANTMENTS.register("back_2_square_1", () -> new ModEnchantmentBuilder()
                    .rarity(Rarity.COMMON).category(EnchantmentCategory.BREAKABLE)
                    .addCompatibleSlot(EquipmentSlot.MAINHAND).addCompatibleSlot(EquipmentSlot.OFFHAND)
                    .setMaxLevel(1).isCurse().build());
    public static final RegistryObject<Enchantment> ENCHANT_TOTAL_ERADICATION =
            MODENCHANTMENTS.register("total_eradication", () -> new ModEnchantmentBuilder()
                    .rarity(Rarity.RARE).category(EnchantmentCategory.WEAPON)
                    .addCompatibleSlot(EquipmentSlot.MAINHAND).setMaxLevel(5)
                    .hasDamageBonus().damageBonusIndex(0.25f).build());
    public static final RegistryObject<Enchantment> ENCHANT_WEAKNESS_FOCUS =
            MODENCHANTMENTS.register("weakness_focus", () -> new ModEnchantmentBuilder()
                    .rarity(Rarity.UNCOMMON).category(EnchantmentCategory.WEAPON).setMaxLevel(4).build());
    public static final RegistryObject<Enchantment> ENCHANT_HEAVEN_STRIKE =
            MODENCHANTMENTS.register("heaven_strike",()-> new ModEnchantmentBuilder()
                    .rarity(Rarity.RARE).category(EnchantmentCategory.WEAPON).setMaxLevel(4).build());
    public static final RegistryObject<Enchantment> ENCHANT_REMOTE_BLOCKING =
            MODENCHANTMENTS.register("remote_blocking",() -> new ModEnchantmentBuilder()
                    .rarity(Rarity.UNCOMMON).category(SHIELD_ITEM).setMaxLevel(1).build());
}
