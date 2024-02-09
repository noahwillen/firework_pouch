package net.noah.fireworkpouch.init;


import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.noah.fireworkpouch.FireworkPouch;
import net.noah.fireworkpouch.client.Keybindings;
import net.noah.fireworkpouch.network.PacketHandler;
import net.noah.fireworkpouch.network.RocketKeyPressPacket;


@Mod.EventBusSubscriber(modid = FireworkPouch.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeSetup {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(Keybindings.INSTANCE.rocketkey.consumeClick() && minecraft.player != null) {
            PacketHandler.sendToServer(new RocketKeyPressPacket());
        }
    }
}
