package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import com.github.wintersteve25.tau.menu.TauContainerMenu;
import com.github.wintersteve25.tau.menu.UIMenu;
import com.github.wintersteve25.tau.utils.SimpleVec2i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ONIBaseMenu implements UIMenu {
    @Override
    public ItemStack quickMoveStack(TauContainerMenu menu, Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = menu.getSlot(index);

        if (!(menu.getBlockEntity() instanceof CoalGenTE coalGenTE)) {
            return itemstack;
        }

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            int startPlayerInvIndex = coalGenTE.getInvSize();
            int startPlayerHBIndex = startPlayerInvIndex + 27;
            int endPlayerInvIndex = menu.slots.size();
            int startMachineIndex = 0;

            // shift-clicked slot that belongs to the machine inventory
            if (index >= startMachineIndex && index < startPlayerInvIndex) {
                // transfer to player inventory, prioritize the hotbar
                if (!menu.moveItemStackTo(stack, startPlayerInvIndex, endPlayerInvIndex, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                // shift-clicked player hotbar slots
                if (index >= startPlayerHBIndex) {
                    // try to move to machine inventory first, if cant, then the player non-hotbar inventory
                    if (!menu.moveItemStackTo(stack, startMachineIndex, startPlayerHBIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (index >= startPlayerInvIndex && index < startPlayerHBIndex) {
                    // tries to put in machine
                    if (!menu.moveItemStackTo(stack, startMachineIndex, startPlayerInvIndex, false)) {
                        // if not, tries hotbar
                        if (!menu.moveItemStackTo(stack, startPlayerHBIndex, endPlayerInvIndex, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }
    
    @Override
    public SimpleVec2i getSize() {
        return new SimpleVec2i(200, 200);
    }
}
