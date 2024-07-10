package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Modified from https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/tile/TileEntityBoundingBlock.java
 * Compatible with MIT License https://github.com/mekanism/Mekanism/blob/1.16.x/LICENSE
 */
public class ONIBoundingTE extends ONIBaseTE {

    private BlockPos mainPos;
    public boolean receivedCoords;

    public ONIBoundingTE(BlockPos pos, BlockState state) {
        this(ONIBlocks.BOUNDING_TE.get(), pos, state);
    }

    public ONIBoundingTE(BlockEntityType<ONIBoundingTE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.mainPos = BlockPos.ZERO;
    }

    public void setMainLocation(BlockPos pos) {
        this.receivedCoords = pos != null;
        if (this.isServer()) {
            this.mainPos = pos;
            this.sendNBTUpdatePacket();
        }
    }

    public BlockPos getMainPos() {
        if (this.mainPos == null) {
            this.mainPos = BlockPos.ZERO;
        }

        return this.mainPos;
    }

    @Nullable
    public BlockEntity getMainTile() {
        return this.receivedCoords ? this.level.getBlockEntity(this.getMainPos()) : null;
    }

    @Override
    public void readSavedAndSyncedData(@Nonnull CompoundTag nbtTags, HolderLookup.Provider provider) {
        super.readSavedAndSyncedData(nbtTags, provider);
        Optional<BlockPos> pos = NbtUtils.readBlockPos(nbtTags, "main");
        pos.ifPresent(p -> this.mainPos = p);
        this.receivedCoords = nbtTags.getBoolean("receivedCoords");
    }

    @Override
    public void writeSavedAndSyncedData(@Nonnull CompoundTag nbtTags, HolderLookup.Provider provider) {
        super.writeSavedAndSyncedData(nbtTags, provider);
        nbtTags.put("main", NbtUtils.writeBlockPos(this.getMainPos()));
        nbtTags.putBoolean("receivedCoords", this.receivedCoords);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag tag, HolderLookup.Provider provider) {
        super.handleUpdateTag(tag, provider);
        Optional<BlockPos> pos = NbtUtils.readBlockPos(tag, "main");
        pos.ifPresent(p -> this.mainPos = p);
        receivedCoords = tag.getBoolean("receivedCoords");
    }
}
