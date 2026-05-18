package com.kochab02.moreThanYAC.client.renderer;

import com.kochab02.moreThanYAC.MoreThanYAC;
import com.kochab02.moreThanYAC.entity.EntityDemon;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;

public class EntityDemonRenderer extends MobRenderer<EntityDemon, HumanoidModel<EntityDemon>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreThanYAC.MODID,"textures/entity/demon.png");

    public EntityDemonRenderer(EntityRendererProvider.Context context){
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);

        this.addLayer(new ItemInHandLayer<>(this,context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDemon entityDemon){
        return TEXTURE;
    }
}