package net.mobz.Items.Item;

import java.util.List;
import java.util.Random;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.mobz.glomod;
import net.mobz.Items.SwordItems;

public class Whitebag extends Item {
    public Whitebag(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("item.mobz.whitebag.tooltip"));
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {

        Random random = new Random();
        int randomNumber = (random.nextInt() + 7) % 3;

        if (!world.isClient && randomNumber == 0) {
            return new ItemStack(SwordItems.Axe);
        } else {
            if (!world.isClient && randomNumber == 1) {
                return new ItemStack(glomod.SHIELD);
            } else {
                if (!world.isClient && randomNumber == 2) {
                    return new ItemStack(SwordItems.Sword);
                } else {
                    return new ItemStack(SwordItems.BossSword);
                }
            }
        }
    }

    public int getMaxUseTime(ItemStack itemStack_1) {
        return 1;
    }

    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        playerEntity_1.setCurrentHand(hand_1);
        return new TypedActionResult(ActionResult.SUCCESS, playerEntity_1.getStackInHand(hand_1));
    }
}