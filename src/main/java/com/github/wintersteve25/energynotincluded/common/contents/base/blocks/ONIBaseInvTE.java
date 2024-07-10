package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIHasValidItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public abstract class ONIBaseInvTE extends ONIBaseTE {

    protected final ItemStackHandler itemHandler = new ONIInventoryHandler(this);

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
    public void readSavedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.readSavedData(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("inv"));
    }

    @Override
    public void writeSavedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.writeSavedData(tag, provider);
        tag.put("inv", itemHandler.serializeNBT(provider));
    }

    public static class ONIInventoryHandler extends ItemStackHandler {
        private final ONIBaseInvTE tile;

        public ONIInventoryHandler(ONIBaseInvTE inv) {
            super(inv.getInvSize());
            tile = inv;
        }

        @Override
        public void onContentsChanged(int slot) {
            tile.updateBlock();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (!(tile instanceof ONIIHasValidItems validItems)) {
                return true;
            }
            BiPredicate<ItemStack, Integer> valids = validItems.validItemsPredicate();
            return valids.test(stack, slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!(tile instanceof ONIIHasValidItems validItems)) {
                return super.insertItem(slot, stack, simulate);
            }
            BiPredicate<ItemStack, Integer> valids = validItems.validItemsPredicate();
            return valids.test(stack, slot) ? super.insertItem(slot, stack, simulate) : stack;
        }
    }
}
