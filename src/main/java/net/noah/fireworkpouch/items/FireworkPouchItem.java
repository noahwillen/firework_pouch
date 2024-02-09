package net.noah.fireworkpouch.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class FireworkPouchItem extends Item{
    private static final int MAX_CONTENT = 576;

    public FireworkPouchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = pSlot.getItem();
            if (itemstack.isEmpty()) {
                //remove one stack
                ItemStack removedStack = removeStack(pStack);
                if (removedStack != null) {
                    this.playRemoveSound(pPlayer);
                    pSlot.safeInsert(removedStack);
                }
            } else if (itemstack.getItem() instanceof FireworkRocketItem && itemstack.getOrCreateTagElement("Fireworks").contains("Flight")) {
                //add one stack
                int amountAdded = tryAdd(pStack, itemstack);
                if (amountAdded>0) {
                    this.playInsertSound(pPlayer);
                    pSlot.safeTake(itemstack.getCount(), amountAdded, pPlayer);
                }
            }

            return true;
        }
    }

    private static int tryAdd(ItemStack pBundleStack, ItemStack pInsertedStack) {
        CompoundTag compoundtag = pBundleStack.getOrCreateTag();
        if (!compoundtag.contains("flight")) {
            compoundtag.putByte("flight", pInsertedStack.getTagElement("Fireworks").getByte("Flight"));
        } else if (pInsertedStack.getTagElement("Fireworks").getByte("Flight") != compoundtag.getByte("flight")) {
            return 0;
        }

        if (!compoundtag.contains("count")) {
            compoundtag.putInt("count", pInsertedStack.getCount());
            return pInsertedStack.getCount();
        } else if (compoundtag.getInt("count")+pInsertedStack.getCount() <= MAX_CONTENT) {
            compoundtag.putInt("count", compoundtag.getInt("count")+pInsertedStack.getCount());
            return pInsertedStack.getCount();
        }


        return 0;
    }

    private static ItemStack removeStack(ItemStack pBundleStack) {
        CompoundTag compoundtag = pBundleStack.getOrCreateTag();
        if (compoundtag.contains("count") && compoundtag.getInt("count") > 0) {
            if (compoundtag.getInt("count") > 64) {
                ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, 64);
                itemStack.getOrCreateTagElement("Fireworks").putByte("Flight", compoundtag.getByte("flight"));
                compoundtag.putInt("count", compoundtag.getInt("count")-64);
                return itemStack;
            } else {
                ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, compoundtag.getInt("count"));
                itemStack.getOrCreateTagElement("Fireworks").putByte("Flight", compoundtag.getByte("flight"));
                compoundtag.remove("flight");
                compoundtag.putInt("count", 0);
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag compoundTag = pStack.getOrCreateTag();
        if (compoundTag.contains("count")) {
            pTooltipComponents.add(Component.translatable("fireworkpouch.tooltip.count").append(": ").append(String.valueOf(compoundTag.getInt("count"))));
        }
        if (compoundTag.contains("flight")) {
            pTooltipComponents.add(Component.translatable("fireworkpouch.tooltip.flight_duration").append(": ").append(String.valueOf(compoundTag.getByte("flight"))));
        }
    }

    private void playRemoveSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }
}
