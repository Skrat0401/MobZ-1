package net.mobz.Entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.mobz.glomod;

public class Boar extends PigEntity {
    private static final Ingredient BREEDING_INGREDIENT;

    public Boar(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
    }

    public boolean canBeControlledByRider() {
        return false;
    }

    public boolean canSpawn(WorldView viewableWorld_1) {
        BlockPos entityPos = new BlockPos(this.getX(), this.getY() - 1, this.getZ());
        return viewableWorld_1.intersectsEntities(this) && !viewableWorld_1.containsFluid(this.getBoundingBox())
                && !viewableWorld_1.isAir(entityPos);

    }

    protected SoundEvent getAmbientSound() {
        return glomod.BOARSAYEVENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource_1) {
        return glomod.BOARSAYEVENT;
    }

    protected SoundEvent getDeathSound() {
        return glomod.BOARDEATHEVENT;
    }

    public boolean isSaddled() {
        return false;
    }

    public Boar method_6574(PassiveEntity passiveEntity_1) {
        return (Boar) glomod.BOAR.create(this.world);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    public Boar createChild(PassiveEntity passiveEntity) {
        return (Boar) glomod.BOAR.create(this.world);
    }

    static {

        BREEDING_INGREDIENT = Ingredient.ofItems(Items.CARROT, Items.POTATO, Items.BEETROOT);
    }

}
