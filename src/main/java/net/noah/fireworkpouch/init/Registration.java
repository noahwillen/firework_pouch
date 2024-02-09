package net.noah.fireworkpouch.init;


import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.noah.fireworkpouch.FireworkPouch;
import net.noah.fireworkpouch.items.FireworkPouchItem;

public class Registration {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FireworkPouch.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FireworkPouch.MOD_ID);


    public static final RegistryObject<Item> FIREWORK_POUCH_ITEM = ITEMS.register("firework_pouch", () -> new FireworkPouchItem(new Item.Properties().stacksTo(1)));

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static final RegistryObject<CreativeModeTab> FIREWORK_POUCH_TAB = CREATIVE_MODE_TABS.register("fireworkpouch_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> FIREWORK_POUCH_ITEM.get().asItem().getDefaultInstance())
            .title(Component.translatable("creativetab.fireworkpouch"))
            .displayItems((parameters, output) -> {
                output.accept(Registration.FIREWORK_POUCH_ITEM.get());
            }).build());

}