package com.kochab02.moreThanYAC.utils;

import com.kochab02.moreThanYAC.MoreThanYAC;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageType {
    public static final ResourceKey<DamageType> DEMON =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MoreThanYAC.MODID,"demon"));
    public static final ResourceKey<DamageType> DEMON_SLASH =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MoreThanYAC.MODID,"demon_slash"));
    public static final ResourceKey<DamageType> ELEMENTAL =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MoreThanYAC.MODID,"elemental"));
}