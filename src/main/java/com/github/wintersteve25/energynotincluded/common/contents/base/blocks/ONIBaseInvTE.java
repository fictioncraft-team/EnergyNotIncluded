package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ONIInventoryHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ONIBaseInvTE extends ONIBaseTE {

    protected final ItemStackHandler itemHandler = new ONIInventoryHandler(this);
    protected final LazyOptional<IItemHandler> itemLazyOptional = LazyOptional.of(() -> itemHandler);

    public ONIBaseInvTE(BlockEntityType<?> te, BlockPos pos, BlockState state) {
        super(te, pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public abstract int getInvSize();

    public boolean hasItem() {
        for (int i = 0; i < getInvSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inv"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", itemHandler.serializeNBT());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
