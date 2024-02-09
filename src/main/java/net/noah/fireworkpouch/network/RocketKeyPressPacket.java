package net.noah.fireworkpouch.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import net.noah.fireworkpouch.items.FireworkPouchItem;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Optional;
import java.util.function.Supplier;

public class RocketKeyPressPacket {
    public RocketKeyPressPacket() {

    }

    public RocketKeyPressPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer pPlayer = context.getSender();
            ServerLevel pLevel = (ServerLevel) pPlayer.level();
            if (pPlayer.isFallFlying()) {
                ItemStack itemStack = findPouchOrStack(pPlayer);
                if (itemStack == null) {
                    return;
                }
                if (itemStack.getItem() instanceof FireworkPouchItem pouch) {
                    byte b = itemStack.getOrCreateTag().getByte("flight");
                    itemStack.getOrCreateTag().putInt("count", itemStack.getOrCreateTag().getInt("count")-1);
                    if (itemStack.getOrCreateTag().getInt("count")==0) {
                        itemStack.getOrCreateTag().remove("flight");
                    }
                    itemStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
                    itemStack.getOrCreateTagElement("Fireworks").putByte("Flight", b);
                } else {
                    itemStack.shrink(1);
                }

                FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(pLevel, itemStack, pPlayer);
                pPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                pLevel.addFreshEntity(fireworkrocketentity);
            }
        });
    }

    public ItemStack findPouchOrStack(Player pPlayer) {
        Inventory pPlayerInv = pPlayer.getInventory();

        if (ModList.get().isLoaded("curios")) {
            top.theillusivec4.curios.api.type.util.ICuriosHelper curiosHelper =  top.theillusivec4.curios.api.CuriosApi.getCuriosHelper();
            Optional<ISlotType> optionalType =  top.theillusivec4.curios.api.CuriosApi.getSlotHelper().getSlotType("belt");
            if (optionalType.isPresent()) {
                for (int i=0; i<optionalType.get().getSize();i++) {
                    ItemStack itemStack = curiosHelper.getEquippedCurios(pPlayer).resolve().get().getStackInSlot(i);
                    if (itemStack.getItem() instanceof FireworkPouchItem && itemStack.getOrCreateTag().contains("count") && itemStack.getOrCreateTag().getInt("count")>0) {
                        return itemStack;
                    }
                }
            }
        }

        for (ItemStack itemStack : pPlayerInv.items) {
            if (itemStack.getItem() instanceof FireworkPouchItem && itemStack.getOrCreateTag().contains("count") && itemStack.getOrCreateTag().getInt("count")>0) {
                return itemStack;
            }
        }
        for (ItemStack itemStack : pPlayerInv.items) {
            if (itemStack.getItem() instanceof FireworkRocketItem) {
                return itemStack;
            }
        }

        return null;
    }
}
