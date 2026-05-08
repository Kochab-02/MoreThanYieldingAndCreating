package com.name.moreThanYAC.client.renderer;

import com.name.moreThanYAC.MoreThanYAC;
import com.name.moreThanYAC.entity.EntityCorruptedDemon;
import com.name.moreThanYAC.entity.EntityDemon;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class EntityCorruptedDemonRenderer extends MobRenderer<EntityCorruptedDemon, HumanoidModel<EntityCorruptedDemon>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreThanYAC.MODID,"textures/entity/corrupted_demon.png");

    public EntityCorruptedDemonRenderer(EntityRendererProvider.Context context){
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);

        this.addLayer(new ItemInHandLayer<>(this,context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorruptedDemon entityDemon){
        return TEXTURE;
    }
}