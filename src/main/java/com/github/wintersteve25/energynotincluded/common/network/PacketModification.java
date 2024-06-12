package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications.ONIModificationGUI;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications.ONIModificationItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketModification(ItemStack mod, int bonusData) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketModification> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "modification"));
    public static final StreamCodec<ByteBuf, PacketModification> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ItemStack.CODEC),
            PacketModification::mod,
            ByteBufCodecs.VAR_INT,
            PacketModification::bonusData,
            PacketModification::new
    );

    public static void handleOnClient(final PacketModification data, final IPayloadContext context) {
        context.enqueueWork(() -> ONIModificationGUI.open(data.mod, data.bonusData))
                .exceptionally(e -> {
                    context.disconnect(Component.literal(e.getLocalizedMessage()));
                    return null;
                });
    }

    public static void handleOnServer(final PacketModification data, final IPayloadContext context) {
        context.enqueueWork(() -> ONIModificationItem.setBonusDataToItemStack((ServerPlayer) context.player(), data.bonusData))
                .exceptionally(e -> {
                    context.disconnect(Component.literal(e.getLocalizedMessage()));
                    return null;
                });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
