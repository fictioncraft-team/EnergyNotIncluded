package com.github.wintersteve25.energynotincluded.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIForceStoppable;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIHasRedstoneOutput;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateServerBE(BlockPos pos, byte packetType, int thresholdValue) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketUpdateServerBE> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "updateServerBE"));
    public static final StreamCodec<ByteBuf, PacketUpdateServerBE> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PacketUpdateServerBE::pos,
            ByteBufCodecs.BYTE,
            PacketUpdateServerBE::packetType,
            ByteBufCodecs.INT,
            PacketUpdateServerBE::thresholdValue,
            PacketUpdateServerBE::new
    );

    public PacketUpdateServerBE(BlockEntity teIn, byte packetType) {
        this(teIn.getBlockPos(), packetType, 0);
    }

    public static void handle(PacketUpdateServerBE data, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (data.pos == null) {
                return;
            }

            ServerPlayer player = (ServerPlayer) ctx.player();
            ONIBaseTE te = (ONIBaseTE) player.getCommandSenderWorld().getBlockEntity(data.pos);

            if (te == null) {
                return;
            }


            switch (data.packetType) {
                case ONIConstants.PacketType.REDSTONE_INPUT:
                    if (te instanceof ONIIForceStoppable forceStoppable) {
                        forceStoppable.toggleInverted();
                    }
                    break;
                case ONIConstants.PacketType.REDSTONE_OUTPUT_LOW:
                    if (te instanceof ONIIHasRedstoneOutput tile) {
                        tile.setLowThreshold(data.thresholdValue);
                    } else {
                        ONIUtils.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", data.pos);
                    }
                    break;
                case ONIConstants.PacketType.REDSTONE_OUTPUT_HIGH:
                    if (te instanceof ONIIHasRedstoneOutput tile) {
                        tile.setHighThreshold(data.thresholdValue);
                    } else {
                        ONIUtils.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", data.pos);
                    }
                    break;
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
