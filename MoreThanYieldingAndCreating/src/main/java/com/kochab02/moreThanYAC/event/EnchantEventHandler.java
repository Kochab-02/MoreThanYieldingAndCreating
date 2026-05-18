package com.kochab02.moreThanYAC.event;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.registries.ModEnchantmentHandler;
import com.kochab02.moreThanYAC.utils.ModConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EnchantEventHandler
{

    @SubscribeEvent
    public static void onQianGongJinQiFishing(ItemFishedEvent event){
        Player victim = event.getEntity();
        Random random = new Random(System.currentTimeMillis());
        if (victim.level().isClientSide()) return;
        ItemStack item = victim.getItemInHand(victim.getUsedItemHand());
        if (item.getItem() instanceof FishingRodItem
                && item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_BACK_TO_SQUARE_ONE.get())>0) {
            if (ModConfig.ENABLE_COMPENSATION.get()){
                victim.addEffect(new MobEffectInstance(
                                MobEffects.LUCK, 100, 0, true, true));
            }
            if (random.nextInt(10)<7) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onQianGongJinQiDigging(BreakEvent event){
        Player victim = event.getPlayer();
        Random random = new Random(System.currentTimeMillis());
        if (victim.level().isClientSide()) return;
        ItemStack item = victim.getItemInHand(victim.getUsedItemHand());
        if (item.getItem() instanceof DiggerItem
                && item.getEnchantmentLevel(ModEnchantmentHandler.ENCHANT_BACK_TO_SQUARE_ONE.get())>0){
            if (random.nextInt(10)<4) {
                float hardness = event.getState().getDestroySpeed(event.getLevel(), event.getPos());
                if (hardness >= 2f) {
                    if (ModConfig.ENABLE_COMPENSATION.get()) {
                        victim.addEffect(new MobEffectInstance(
                                MobEffects.DIG_SPEED, 100, 1, true, true));
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
}
