package com.github.wintersteve25.energynotincluded.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ONINetworking {
    public static void registerMessages(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
                PacketModification.TYPE,
                PacketModification.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        PacketModification::handleOnClient,
                        PacketModification::handleOnServer
                )
        );

        registrar.playToClient(
                PacketOpenUI.TYPE,
                PacketOpenUI.CODEC,
                PacketOpenUI::handle
        );

        registrar.playToClient(
                PacketRenderError.TYPE,
                PacketRenderError.CODEC,
                PacketRenderError::handle
        );

        registrar.playToServer(
                PacketSetBlueprintRecipe.TYPE,
                PacketSetBlueprintRecipe.CODEC,
                PacketSetBlueprintRecipe::handle
        );

        registrar.playToClient(
                PacketUpdateClientBE.TYPE,
                PacketUpdateClientBE.CODEC,
                PacketUpdateClientBE::handle
        );

        registrar.playToServer(
                PacketUpdateServerBE.TYPE,
                PacketUpdateServerBE.CODEC,
                PacketUpdateServerBE::handle
        );
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
