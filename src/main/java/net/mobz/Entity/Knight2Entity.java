package net.mobz.Entity;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.mobz.glomod;
import net.mobz.Items.SwordItems;

public class Knight2Entity extends VindicatorEntity {

    public Knight2Entity(EntityType<? extends VindicatorEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;

    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(38.0D);
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    public void onDeath(DamageSource damageSource_1) {
        return;
    }
/*
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, AbstractTraderEntity.class, true));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));

    } */

    protected void initEquipment(LocalDifficulty localDifficulty_1) {
        super.initEquipment(localDifficulty_1);
        if (this.world.getDifficulty() != Difficulty.PEACEFUL) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(SwordItems.PoisonSword));
            this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        }
    }

    public EntityGroup getGroup() {
        return EntityGroup.DEFAULT;
    }
/*
    protected void dropEquipment(DamageSource damageSource_1, int int_1, boolean boolean_1) {
        return;
    } */

    protected SoundEvent getAmbientSound() {
        return glomod.NOTHINGEVENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource_1) {
        return SoundEvents.ENTITY_PLAYER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }

    public boolean canSpawn(WorldView viewableWorld_1) {
        BlockPos entityPos = new BlockPos(this.getX(), this.getY() - 1, this.getZ());
        BlockPos lighto = new BlockPos(this.getX(), this.getY(), this.getZ());
        return viewableWorld_1.intersectsEntities(this) && !viewableWorld_1.containsFluid(this.getBoundingBox())
                && !viewableWorld_1.isAir(entityPos)
                && this.world.getLocalDifficulty(entityPos).getGlobalDifficulty() != Difficulty.PEACEFUL
                && this.world.getLightLevel(lighto) < 9;
    }

}
