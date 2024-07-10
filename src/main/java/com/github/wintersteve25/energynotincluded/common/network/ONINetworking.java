package com.github.wintersteve25.energynotincluded.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ONINetworking {
    public static void registerMessages(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                PacketOpenUI.TYPE,
                PacketOpenUI.CODEC,
                PacketOpenUI::handle
        );

        registrar.playToClient(
                PacketUpdateClientBE.TYPE,
                PacketUpdateClientBE.CODEC,
                PacketUpdateClientBE::handle
        );
        
        registrar.playToServer(
                PacketUpdateBlueprintItem.TYPE,
                PacketUpdateBlueprintItem.CODEC,
                PacketUpdateBlueprintItem::handle
        );
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
