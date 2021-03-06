package net.mobz.Renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.mobz.Entity.*;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnderZombieRenderer extends BipedEntityRenderer<EnderZombieEntity, ZombieEntityModel<EnderZombieEntity>> {
   private static final Identifier SKIN = new Identifier("mobz:textures/entity/enderzombie.png");

   public EnderZombieRenderer(EntityRenderDispatcher entityRenderDispatcher) {
      super(entityRenderDispatcher, new ZombieEntityModel(0.0F, false), 0.5F);
      this.addFeature(
            new ArmorBipedFeatureRenderer(this, new ZombieEntityModel(0.5F, true), new ZombieEntityModel(1.0F, true)));
   }

   public Identifier getTexture(EnderZombieEntity Endy) {
      return SKIN;
   }
}
