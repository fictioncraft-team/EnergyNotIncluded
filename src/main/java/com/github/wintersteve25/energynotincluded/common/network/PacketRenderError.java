package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.client.gui.ONIBaseGuiTabModification;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketRenderError() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketRenderError> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "renderError"));
    public static final StreamCodec<ByteBuf, PacketRenderError> CODEC = StreamCodec.unit(new PacketRenderError());

    public static void handle(PacketRenderError data, IPayloadContext context) {
        context.enqueueWork(ONIBaseGuiTabModification::addError);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
