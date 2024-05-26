package com.github.wintersteve25.energynotincluded.common.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIForceStoppable;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIHasRedstoneOutput;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;

import java.util.function.Supplier;

public class PacketUpdateServerBE {

    private final BlockPos pos;
    private final byte packetType;
    private final int thresholdValue;

    public PacketUpdateServerBE(BlockEntity teIn, byte packetType) {
        this.pos = teIn.getBlockPos();
        this.packetType = packetType;
        this.thresholdValue = 0;
    }

    public PacketUpdateServerBE(BlockEntity teIn, byte packetType, int thresholdValueIn) {
        this.pos = teIn.getBlockPos();
        this.packetType = packetType;
        this.thresholdValue = thresholdValueIn;
    }

    public PacketUpdateServerBE(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.packetType = buffer.readByte();
        this.thresholdValue = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeByte(packetType);
        buffer.writeInt(thresholdValue);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (pos != null) {
                ServerPlayer player = ctx.get().getSender();
                ONIBaseTE te = (ONIBaseTE) player.getCommandSenderWorld().getBlockEntity(pos);
                if (te != null) {
                    switch (packetType) {
                        case ONIConstants.PacketType.REDSTONE_INPUT:
                            if (te instanceof ONIIForceStoppable forceStoppable) {
                                forceStoppable.toggleInverted();
                            }
                            break;
                        case ONIConstants.PacketType.REDSTONE_OUTPUT_LOW:
                            if (te instanceof ONIIHasRedstoneOutput tile) {
                                tile.setLowThreshold(thresholdValue);
                            } else {
                                ONIUtils.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", pos);
                            }
                            break;
                        case ONIConstants.PacketType.REDSTONE_OUTPUT_HIGH:
                            if (te instanceof ONIIHasRedstoneOutput tile) {
                                tile.setHighThreshold(thresholdValue);
                            } else {
                                ONIUtils.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", pos);
                            }
                            break;
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
