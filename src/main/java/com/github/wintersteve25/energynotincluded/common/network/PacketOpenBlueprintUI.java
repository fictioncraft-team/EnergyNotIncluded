package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintGui;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenBlueprintUI {
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ONIBlueprintGui::open);
        ctx.get().setPacketHandled(true);
    }
}
