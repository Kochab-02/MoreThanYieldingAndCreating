package com.kochab02.moreThanYAC.client;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.client.renderer.EntityCorruptedDemonRenderer;
import com.kochab02.moreThanYAC.client.renderer.EntityDemonRenderer;
import com.kochab02.moreThanYAC.registries.ModEntityHandler;
import com.kochab02.moreThanYAC.registries.ModItemHandler;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntityHandler.ENTITY_DEMON.get(), EntityDemonRenderer::new);
        event.registerEntityRenderer(ModEntityHandler.ENTITY_CORRUPTED_DEMON.get(), EntityCorruptedDemonRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItemHandler.DEFENSIVE_SHIELD.get(),
                    ResourceLocation.fromNamespaceAndPath("minecraft", "blocking"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            // 关键：注册 shield 属性，即使值总是 0
            ItemProperties.register(ModItemHandler.DEFENSIVE_SHIELD.get(),
                    ResourceLocation.fromNamespaceAndPath("minecraft", "shield"),
                    (stack, level, entity, seed) -> 0.0F);
            System.out.println("Registered blocking property for DEFENSIVE_SHIELD");
        });
    }
}