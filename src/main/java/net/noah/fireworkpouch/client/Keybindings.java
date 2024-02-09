package net.noah.fireworkpouch.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.noah.fireworkpouch.FireworkPouch;

public final class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings() {}

    public final KeyMapping rocketkey = new KeyMapping(
            "key." + FireworkPouch.MOD_ID + ".rocket_key",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            "key.categories." + FireworkPouch.MOD_ID
    );
}
