package net.mobz.Entity;

import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.mobz.glomod;

public class MageEntity extends SpellcastingIllagerEntity {
   private SheepEntity wololoTarget;

   public MageEntity(EntityType<? extends MageEntity> entityType, World world) {
      super(entityType, world);
      this.experiencePoints = 10;
   }

   protected void initGoals() {
      super.initGoals();
      this.goalSelector.add(0, new SwimGoal(this));
      this.goalSelector.add(1, new MageEntity.LookAtTargetOrWololoTarget());
      this.goalSelector.add(2, new FleeEntityGoal(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
      this.goalSelector.add(4, new MageEntity.SummonSpider());
      this.goalSelector.add(5, new MageEntity.ConjureFangsGoal());
      this.goalSelector.add(6, new MageEntity.WololoGoal());
      this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
      this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
      this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
      this.targetSelector.add(1, (new RevengeGoal(this, new Class[] { RaiderEntity.class })).setGroupRevenge());
      this.targetSelector.add(2,
            (new FollowTargetGoal(this, PlayerEntity.class, true)).setMaxTimeWithoutVisibility(300));
      this.targetSelector.add(3,
            (new FollowTargetGoal(this, AbstractTraderEntity.class, false)).setMaxTimeWithoutVisibility(300));
      this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, false));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
      this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
      this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(24.0D);
   }

   protected void initDataTracker() {
      super.initDataTracker();
   }

   public boolean canSpawn(WorldView viewableWorld_1) {
      BlockPos entityPos = new BlockPos(this.getX(), this.getY() - 1, this.getZ());
      return viewableWorld_1.intersectsEntities(this) && !viewableWorld_1.containsFluid(this.getBoundingBox())
            && !viewableWorld_1.isAir(entityPos)
            && this.world.getLocalDifficulty(entityPos).getGlobalDifficulty() != Difficulty.PEACEFUL
            && this.world.isDay();

   }

   public void readCustomDataFromTag(CompoundTag tag) {
      super.readCustomDataFromTag(tag);
   }

   public SoundEvent getCelebratingSound() {
      return SoundEvents.ENTITY_EVOKER_CELEBRATE;
   }

   public void writeCustomDataToTag(CompoundTag tag) {
      super.writeCustomDataToTag(tag);
   }

   protected void mobTick() {
      super.mobTick();
   }

   public boolean isTeammate(Entity entity_1) {
      if (entity_1 == null) {
         return false;
      } else if (entity_1 == this) {
         return true;
      } else if (super.isTeammate(entity_1)) {
         return true;
      } else if (entity_1 instanceof SpiSmall) {
         return true;
      } else {
         return false;
      }
   }

   protected SoundEvent getAmbientSound() {
      return glomod.EVEIDLEEVENT;
   }

   protected SoundEvent getDeathSound() {
      return glomod.EVEDEATHEVENT;
   }

   protected SoundEvent getHurtSound(DamageSource damageSource_1) {
      return glomod.EVEHURTEVENT;
   }

   private void setWololoTarget(@Nullable SheepEntity sheep) {
      this.wololoTarget = sheep;
   }

   @Nullable
   private SheepEntity getWololoTarget() {
      return this.wololoTarget;
   }

   protected SoundEvent getCastSpellSound() {
      return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
   }

   public void addBonusForWave(int wave, boolean unused) {
   }

   public class WololoGoal extends SpellcastingIllagerEntity.CastSpellGoal {
      private final TargetPredicate purpleSheepPredicate = (new TargetPredicate()).setBaseMaxDistance(16.0D)
            .includeInvulnerable().setPredicate((livingEntity) -> {
               return ((SheepEntity) livingEntity).getColor() == DyeColor.BLUE;
            });

      public WololoGoal() {
         super();
      }

      public boolean canStart() {
         if (MageEntity.this.getTarget() != null) {
            return false;
         } else if (MageEntity.this.isSpellcasting()) {
            return false;
         } else if (MageEntity.this.age < this.startTime) {
            return false;
         } else if (!MageEntity.this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            return false;
         } else {
            List<SheepEntity> list = MageEntity.this.world.getTargets(SheepEntity.class, this.purpleSheepPredicate,
                  MageEntity.this, MageEntity.this.getBoundingBox().expand(16.0D, 4.0D, 16.0D));
            if (list.isEmpty()) {
               return false;
            } else {
               MageEntity.this.setWololoTarget((SheepEntity) list.get(MageEntity.this.random.nextInt(list.size())));
               return true;
            }
         }
      }

      public boolean shouldContinue() {
         return MageEntity.this.getWololoTarget() != null && this.spellCooldown > 0;
      }

      public void stop() {
         super.stop();
         MageEntity.this.setWololoTarget((SheepEntity) null);
      }

      protected void castSpell() {
         SheepEntity sheepEntity = MageEntity.this.getWololoTarget();
         if (sheepEntity != null && sheepEntity.isAlive()) {
            sheepEntity.setColor(DyeColor.RED);
         }

      }

      protected int getInitialCooldown() {
         return 40;
      }

      protected int getSpellTicks() {
         return 60;
      }

      protected int startTimeDelay() {
         return 140;
      }

      protected SoundEvent getSoundPrepare() {
         return SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO;
      }

      protected SpellcastingIllagerEntity.Spell getSpell() {
         return SpellcastingIllagerEntity.Spell.WOLOLO;
      }
   }

   class SummonSpider extends SpellcastingIllagerEntity.CastSpellGoal {
      private final TargetPredicate closeVexPredicate;

      private SummonSpider() {
         super();
         this.closeVexPredicate = (new TargetPredicate()).setBaseMaxDistance(16.0D).includeHidden()
               .ignoreDistanceScalingFactor().includeInvulnerable().includeTeammates();
      }

      public boolean canStart() {
         if (!super.canStart()) {
            return false;
         } else {
            int i = MageEntity.this.world.getTargets(SpiSmall.class, this.closeVexPredicate, MageEntity.this,
                  MageEntity.this.getBoundingBox().expand(16.0D)).size();
            return MageEntity.this.random.nextInt(8) + 1 > i;
         }
      }

      protected int getSpellTicks() {
         return 100;
      }

      protected int startTimeDelay() {
         return 340;
      }

      protected void castSpell() {
         for (int i = 0; i < 3; ++i) {
            BlockPos blockPos = (new BlockPos(MageEntity.this)).add(-2 + MageEntity.this.random.nextInt(5), 1,
                  -2 + MageEntity.this.random.nextInt(5));
            SpiSmall vexEntity = (SpiSmall) glomod.SPISMALL.create(MageEntity.this.world);
            vexEntity.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
            vexEntity.initialize(MageEntity.this.world, MageEntity.this.world.getLocalDifficulty(blockPos),
                  SpawnType.MOB_SUMMONED, (EntityData) null, (CompoundTag) null);
            vexEntity.setOwner(MageEntity.this);
            vexEntity.setBounds(blockPos);
            vexEntity.setLifeTicks(20 * (30 + MageEntity.this.random.nextInt(90)));
            MageEntity.this.world.spawnEntity(vexEntity);
         }

      }

      protected SoundEvent getSoundPrepare() {
         return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
      }

      protected SpellcastingIllagerEntity.Spell getSpell() {
         return SpellcastingIllagerEntity.Spell.SUMMON_VEX;
      }
   }

   class ConjureFangsGoal extends SpellcastingIllagerEntity.CastSpellGoal {
      private ConjureFangsGoal() {
         super();
      }

      protected int getSpellTicks() {
         return 40;
      }

      protected int startTimeDelay() {
         return 100;
      }

      protected void castSpell() {
         LivingEntity livingEntity = MageEntity.this.getTarget();
         double d = Math.min(livingEntity.getY(), MageEntity.this.getY());
         double e = Math.max(livingEntity.getY(), MageEntity.this.getY()) + 1.0D;
         float f = (float) MathHelper.atan2(livingEntity.getZ() - MageEntity.this.getZ(),
               livingEntity.getX() - MageEntity.this.getX());
         int j;
         if (MageEntity.this.squaredDistanceTo(livingEntity) < 9.0D) {
            float h;
            for (j = 0; j < 5; ++j) {
               h = f + (float) j * 3.1415927F * 0.4F;
               this.conjureFangs(MageEntity.this.getX() + (double) MathHelper.cos(h) * 1.5D,
                     MageEntity.this.getZ() + (double) MathHelper.sin(h) * 1.5D, d, e, h, 0);
            }

            for (j = 0; j < 8; ++j) {
               h = f + (float) j * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
               this.conjureFangs(MageEntity.this.getX() + (double) MathHelper.cos(h) * 2.5D,
                     MageEntity.this.getZ() + (double) MathHelper.sin(h) * 2.5D, d, e, h, 3);
            }
         } else {
            for (j = 0; j < 16; ++j) {
               double l = 1.25D * (double) (j + 1);
               int m = 1 * j;
               this.conjureFangs(MageEntity.this.getX() + (double) MathHelper.cos(f) * l,
                     MageEntity.this.getZ() + (double) MathHelper.sin(f) * l, d, e, f, m);
            }
         }

      }

      private void conjureFangs(double x, double z, double maxY, double y, float f, int warmup) {
         BlockPos blockPos = new BlockPos(x, y, z);
         boolean bl = false;
         double d = 0.0D;

         do {
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState = MageEntity.this.world.getBlockState(blockPos2);
            if (blockState.isSideSolidFullSquare(MageEntity.this.world, blockPos2, Direction.UP)) {
               if (!MageEntity.this.world.isAir(blockPos)) {
                  BlockState blockState2 = MageEntity.this.world.getBlockState(blockPos);
                  VoxelShape voxelShape = blockState2.getCollisionShape(MageEntity.this.world, blockPos);
                  if (!voxelShape.isEmpty()) {
                     d = voxelShape.getMaximum(Direction.Axis.Y);
                  }
               }

               bl = true;
               break;
            }

            blockPos = blockPos.down();
         } while (blockPos.getY() >= MathHelper.floor(maxY) - 1);

         if (bl) {
            MageEntity.this.world.spawnEntity(new EvokerFangsEntity(MageEntity.this.world, x,
                  (double) blockPos.getY() + d, z, f, warmup, MageEntity.this));
         }

      }

      protected SoundEvent getSoundPrepare() {
         return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
      }

      protected SpellcastingIllagerEntity.Spell getSpell() {
         return SpellcastingIllagerEntity.Spell.FANGS;
      }
   }

   class LookAtTargetOrWololoTarget extends SpellcastingIllagerEntity.LookAtTargetGoal {
      private LookAtTargetOrWololoTarget() {
         super();
      }

      public void tick() {
         if (MageEntity.this.getTarget() != null) {
            MageEntity.this.getLookControl().lookAt(MageEntity.this.getTarget(),
                  (float) MageEntity.this.getBodyYawSpeed(), (float) MageEntity.this.getLookPitchSpeed());
         } else if (MageEntity.this.getWololoTarget() != null) {
            MageEntity.this.getLookControl().lookAt(MageEntity.this.getWololoTarget(),
                  (float) MageEntity.this.getBodyYawSpeed(), (float) MageEntity.this.getLookPitchSpeed());
         }

      }
   }
}
