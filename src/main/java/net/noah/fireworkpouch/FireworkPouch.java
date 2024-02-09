package net.noah.fireworkpouch;

import net.minecraft.world.item.BundleItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.noah.fireworkpouch.init.Registration;
import net.noah.fireworkpouch.network.PacketHandler;

@Mod(FireworkPouch.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FireworkPouch {

    public static final String MOD_ID = "fireworkpouch";

    public static IEventBus MOD_EVENT_BUS;

    public FireworkPouch() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        MOD_EVENT_BUS.register(Registration.class);

        Registration.init();

        PacketHandler.register();
    }
}