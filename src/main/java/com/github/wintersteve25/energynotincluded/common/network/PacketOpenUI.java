package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.registries.ONIScreens;
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketOpenUI(ONIScreens gui) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketOpenUI> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "openui"));
    public static final StreamCodec<ByteBuf, PacketOpenUI> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ONIScreens.CODEC),
            PacketOpenUI::gui,
            PacketOpenUI::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketOpenUI data, IPayloadContext context) {
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(new ScreenUIRenderer(data.gui().ui, true, ONIScreens.GUI_THEME)));
    }
}
