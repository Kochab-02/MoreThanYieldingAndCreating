package com.name.moreThanYAC.event;

import com.name.moreThanYAC.MoreThanYAC;
import com.name.moreThanYAC.init.ModItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID)
public class PropertyHandler
{
    private static final String will = "MYC_will";
    private static final String hope = "MYC_hope";
    private static final String luck = "MYC_luck";

    @SubscribeEvent
    public static void propertyAttacher(LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        CompoundTag tag = entity.getPersistentData();
        if (!tag.contains(will)){
            tag.putInt(will,100);
        }
        if (!tag.contains(hope)){
            tag.putInt(hope,100);
        }
        if (!tag.contains(luck)){
            tag.putInt(luck,100);
        }
    }

    @SubscribeEvent
    public static void deathPropertySavior(PlayerEvent.Clone event){
        if (!event.isWasDeath()) return;
        CompoundTag oldTag = event.getOriginal().getPersistentData();
        CompoundTag newTag = event.getEntity().getPersistentData();
        if (event.getEntity().level().isClientSide()) return;
        saveProperty(oldTag,newTag,will);
        saveProperty(oldTag,newTag,hope);
        saveProperty(oldTag,newTag,luck);
    }

    private static void saveProperty(CompoundTag oldTag,CompoundTag newTag,String property){
        int remainedProperty = oldTag.getInt(property);
        newTag.putInt(property,remainedProperty);
    }

    @SubscribeEvent
    public static void addingProperty(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        CompoundTag tag = player.getPersistentData();
        ItemStack stack = event.getItemStack();
        if (stack.getItem().equals(ModItemHandler.ITEM_WILL.get())){
            tag.putInt(will,tag.getInt(will)+10);
        } else if (stack.getItem().equals(ModItemHandler.ITEM_HOPE.get())) {
            tag.putInt(hope,tag.getInt(hope)+10);
        } else return;
        System.out.println("current will and hope:"+tag.getInt(will)+","+tag.getInt(hope));
    }
}
