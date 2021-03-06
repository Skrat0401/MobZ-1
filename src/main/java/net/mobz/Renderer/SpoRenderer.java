package net.mobz.Renderer;

import net.mobz.Entity.*;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SpiderEntityRenderer;
import net.minecraft.client.render.entity.feature.SpiderEyesFeatureRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpoRenderer extends SpiderEntityRenderer<SpoEntity> {

    public SpoRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.addFeature(new SpiderEyesFeatureRenderer(this));
    }

    public Identifier getTexture(SpoEntity spoEntity) {
        return new Identifier("mobz:textures/entity/spo.png");
    }
}