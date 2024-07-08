package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.*;
import com.github.wintersteve25.energynotincluded.common.network.PacketUpdateClientBE;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ONIBaseTE extends BlockEntity {

    public ONIBaseTE(BlockEntityType<?> te, BlockPos pos, BlockState state) {
        super(te, pos, state);
    }

    public static <BE extends BlockEntity> void clientTicker(Level level, BlockPos blockPos, BlockState blockState, BE be) {
        if (be instanceof ONIITickableClient tickable) {
            tickable.clientTick(level, blockPos, blockState);
        }

        if (be instanceof ONIITickableCommon tickable) {
            tickable.commonTick(level, blockPos, blockState);
        }
    }

    public static <BE extends BlockEntity> void serverTicker(Level level, BlockPos blockPos, BlockState blockState, BE be) {
        if (be instanceof ONIITickableServer tickable) {
            tickable.serverTick(level, blockPos, blockState);
        }

        if (be instanceof ONIITickableCommon tickable) {
            tickable.commonTick(level, blockPos, blockState);
        }

        if (level == null) return;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        if (!isServer() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            handleUpdateTag(pkt.getTag(), provider);
        }
    }

    protected void sendNBTUpdatePacket() {
        if (isClient()) return;
        if (isRemoved()) return;
        
        PacketDistributor.sendToPlayersTrackingChunk(
                (ServerLevel) level,
                new ChunkPos(getBlockPos()),
                new PacketUpdateClientBE(this, this.getUpdateTag(level.registryAccess()))
        );
    }


    public void updateBlock() {
        if (level == null) return;
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, 2);
        setChanged();
    }

    public boolean isServer() {
        return level != null && !level.isClientSide();
    }

    public boolean isClient() {
        return level != null && level.isClientSide();
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return world.getBlockState(pos).getBlock().canConnectRedstone(state, world, pos, side);
    }
}
