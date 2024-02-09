package net.noah.fireworkpouch.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.noah.fireworkpouch.FireworkPouch;

public class PacketHandler {

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }


    public static final SimpleChannel INSTANCE =  NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(FireworkPouch.MOD_ID, "main"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(RocketKeyPressPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RocketKeyPressPacket::new)
                .encoder(RocketKeyPressPacket::toBytes)
                .consumerMainThread(RocketKeyPressPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }
}
