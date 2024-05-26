package com.github.wintersteve25.energynotincluded.common.network;

import net.minecraftforge.network.NetworkEvent;
import com.github.wintersteve25.energynotincluded.client.gui.ONIBaseGuiTabModification;

import java.util.function.Supplier;

public class PacketRenderError {
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ONIBaseGuiTabModification::addError);
        ctx.get().setPacketHandled(true);
    }
}
