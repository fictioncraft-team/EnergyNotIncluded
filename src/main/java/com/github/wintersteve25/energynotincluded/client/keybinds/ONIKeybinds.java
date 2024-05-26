package com.github.wintersteve25.energynotincluded.client.keybinds;

import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class ONIKeybinds {

    public static void init() {
    }

    private static KeyMapping registerKeyBind(String name, int code, InputConstants.Type type, IKeyConflictContext conflictContext) {
        KeyMapping keyBinding = new KeyMapping(name, conflictContext, type, code, "oniutils.keybinds.category");
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }
}
