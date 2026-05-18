package com.kochab02.moreThanYAC.registries;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.effect.CommonModEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffectHandler
{
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MoreThanYAC.MODID);

    public static final RegistryObject<MobEffect> SANGJINTIANLIANG =
            EFFECTS.register("sangjintianliang",() -> new CommonModEffect(MobEffectCategory.HARMFUL,0xa70f0f));
}
