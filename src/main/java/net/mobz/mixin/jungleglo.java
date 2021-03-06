package net.mobz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.JungleBiome;
import net.mobz.glomod;

@Mixin(JungleBiome.class)
public class jungleglo extends Biome {

	protected jungleglo(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("RETURN"), method = "<init>()V")
	private void init(CallbackInfo info) {
		this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(glomod.FAST, 20, 2, 2));
		this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(glomod.CRIP, 10, 1, 2));
		this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(glomod.SKELI2, 15, 1, 2));
		this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(glomod.KNIGHT2ENTITY, 15, 1, 1));
		this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(glomod.STONEGOLEM, 10, 1, 1));
		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(glomod.BOAR, 10, 2, 4));
		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(glomod.BOAR3, 5, 2, 4));
		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(glomod.BLACKBEAR, 10, 1, 2));
	}

}