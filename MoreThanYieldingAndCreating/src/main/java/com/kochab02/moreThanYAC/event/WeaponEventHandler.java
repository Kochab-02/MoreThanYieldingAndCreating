package com.kochab02.moreThanYAC.event;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.entity.EntityDemon;
import com.kochab02.moreThanYAC.init.ModItemHandler;
import com.kochab02.moreThanYAC.utils.ModDamageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponEventHandler
{
    @SubscribeEvent
    public static void onDemonAttack(LivingAttackEvent event){
        var source = event.getSource();
        if (!(source.getEntity() instanceof EntityDemon demon)) return;
        if (source.is(ModDamageType.DEMON) || source.is(ModDamageType.DEMON_SLASH)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity victim = event.getEntity();
        if (!(demon.getItemBySlot(EquipmentSlot.MAINHAND).getItem().equals(ModItemHandler.DEMON_SWORD.get()))) return;
        event.setCanceled(true);
        var holder = demon.level().registryAccess().
                registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageType.DEMON);
        DamageSource damageSource = new DamageSource(holder, demon);
        victim.hurt(damageSource,event.getAmount()+2f);
    }
}
