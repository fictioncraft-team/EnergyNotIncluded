package com.github.wintersteve25.energynotincluded.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateClientBE(BlockPos pos, CompoundTag nbt) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketUpdateClientBE> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "update_client_be"));
    public static final StreamCodec<ByteBuf, PacketUpdateClientBE> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PacketUpdateClientBE::pos,
            ByteBufCodecs.COMPOUND_TAG,
            PacketUpdateClientBE::nbt,
            PacketUpdateClientBE::new
    );

    public PacketUpdateClientBE(BlockEntity teIn, CompoundTag compoundNBT) {
        this(teIn.getBlockPos(), compoundNBT);
    }

    public static void handle(PacketUpdateClientBE data, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (data.pos == null) {
                ONIUtils.LOGGER.error("Requested update but position is null");
                return;
            }

            ClientLevel level = Minecraft.getInstance().level;

            if (level == null) {
                ONIUtils.LOGGER.error("Requested update at {} but level does not exist", data.pos);
                return;
            }

            BlockEntity te = level.getBlockEntity(data.pos);

            if (te == null) {
                ONIUtils.LOGGER.error("Requested update at {} but no te is found", data.pos);
                return;
            }

            if (data.nbt == null) return;
            te.handleUpdateTag(data.nbt, ctx.player().registryAccess());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
