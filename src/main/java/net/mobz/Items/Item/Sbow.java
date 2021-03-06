package net.mobz.Items.Item;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Sbow extends BowItem {
   public Sbow(Item.Settings settings) {
      super(settings);
      this.addPropertyGetter(new Identifier("pull"), (stack, world, entity) -> {
         if (entity == null) {
            return 0.0F;
         } else {
            return entity.getActiveItem().getItem() != Items.BOW ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
         }
      });
      this.addPropertyGetter(new Identifier("pulling"), (stack, world, entity) -> {
         return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
      });
   }

   public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

      if (selected == true) {
          StatusEffectInstance def = new StatusEffectInstance(StatusEffect.byRawId(1), 0, 0, false, false);
          LivingEntity bob = (LivingEntity) entity;
          bob.addStatusEffect(def);
      }

  }

   public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
      if (user instanceof PlayerEntity) {
         PlayerEntity playerEntity = (PlayerEntity)user;
         boolean bl = playerEntity.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
         ItemStack itemStack = playerEntity.getArrowType(stack);
         if (!itemStack.isEmpty() || bl) {
            if (itemStack.isEmpty()) {
               itemStack = new ItemStack(Items.ARROW);
            }

            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if ((double)f >= 0.1D) {
               boolean bl2 = bl && itemStack.getItem() == Items.ARROW;
               if (!world.isClient) {
                  ArrowItem arrowItem = (ArrowItem)((ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW));
                  ProjectileEntity projectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                  projectileEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, f * 3.0F, 1.0F);
                  if (f == 1.0F) {
                     projectileEntity.setCritical(true);
                  }

                  int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                  if (j > 0) {
                     projectileEntity.setDamage(projectileEntity.getDamage() + (double)j * 0.5D + 0.5D);
                  }

                  int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                  if (k > 0) {
                     projectileEntity.setPunch(k);
                  }

                  if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                     projectileEntity.setOnFireFor(100);
                  }

                  stack.damage(1, (LivingEntity)playerEntity, (Consumer)((p) -> {
                            ((LivingEntity) p).sendToolBreakStatus(playerEntity.getActiveHand());
                  }));
                  if (bl2 || playerEntity.abilities.creativeMode && (itemStack.getItem() == Items.SPECTRAL_ARROW || itemStack.getItem() == Items.TIPPED_ARROW)) {
                     projectileEntity.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY;
                  }

                  world.spawnEntity(projectileEntity);
               }

               world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
               if (!bl2 && !playerEntity.abilities.creativeMode) {
                  itemStack.decrement(1);
                  if (itemStack.isEmpty()) {
                     playerEntity.inventory.removeOne(itemStack);
                  }
               }

               playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            }
         }
      }
   }

   public static float getPullProgress(int useTicks) {
      float f = (float)useTicks / 15.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if (f > 1.0F) {
         f = 1.0F;
      }

      return f;
   }

   public int getMaxUseTime(ItemStack stack) {
      return 72000;
   }

   public UseAction getUseAction(ItemStack stack) {
      return UseAction.BOW;
   }

   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      ItemStack itemStack = user.getStackInHand(hand);
      boolean bl = !user.getArrowType(itemStack).isEmpty();
      if (!user.abilities.creativeMode && !bl) {
         return TypedActionResult.fail(itemStack);
      } else {
         user.setCurrentHand(hand);
         return TypedActionResult.consume(itemStack);
      }
   }

   public Predicate<ItemStack> getProjectiles() {
      return BOW_PROJECTILES;
   }
}
