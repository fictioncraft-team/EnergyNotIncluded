package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.common.registries.ONIScreen;
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ONIClientPayloadHandler {
    public static void handleOpenUI(PacketOpenUI data, IPayloadContext context) {
        Minecraft.getInstance().setScreen(new ScreenUIRenderer(data.gui().ui.get().get(), true, ONIScreen.GUI_THEME));
    }
}
