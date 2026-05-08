package com.name.moreThanYAC.init;

import com.name.moreThanYAC.MoreThanYAC;
import com.name.moreThanYAC.entity.EntityCorruptedDemon;
import com.name.moreThanYAC.entity.EntityDemon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MoreThanYAC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityHandler
{
    public static final DeferredRegister<EntityType<?>> MODENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MoreThanYAC.MODID);

    public static final RegistryObject<EntityType<EntityDemon>> ENTITY_DEMON =
            MODENTITIES.register("demon", () -> EntityType.Builder.of(EntityDemon::new, MobCategory.MONSTER)
                    .sized(0.8f,1.8f).build("demon"));
    public static final RegistryObject<EntityType<EntityCorruptedDemon>> ENTITY_CORRUPTED_DEMON =
            MODENTITIES.register("corrupted_demon", () -> EntityType.Builder.of(EntityCorruptedDemon::new,
                    MobCategory.MONSTER).sized(0.8f,1.8f).build("corrupted_demon"));

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ENTITY_DEMON.get(), EntityDemon.createAttributes().build());
        event.put(ENTITY_CORRUPTED_DEMON.get(),EntityCorruptedDemon.createAttributes().build());
    }
}
