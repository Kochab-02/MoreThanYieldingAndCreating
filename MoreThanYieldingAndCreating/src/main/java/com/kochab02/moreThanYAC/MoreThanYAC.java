package com.kochab02.moreThanYAC;

import com.kochab02.moreThanYAC.registries.ModEffectHandler;
import com.kochab02.moreThanYAC.registries.ModEnchantmentHandler;
import com.kochab02.moreThanYAC.registries.ModEntityHandler;
import com.kochab02.moreThanYAC.registries.ModItemHandler;
import com.kochab02.moreThanYAC.utils.ModConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(MoreThanYAC.MODID)
public class MoreThanYAC
{
    public static final String MODID = "mtyac";

    public MoreThanYAC(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_CONFIG);

        ModItemHandler.MODITEMS.register(modEventBus);
        ModEntityHandler.MODENTITIES.register(modEventBus);
        ModEnchantmentHandler.MODENCHANTMENTS.register(modEventBus);
        ModEffectHandler.EFFECTS.register(modEventBus);
    }
}
