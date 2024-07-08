package com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal;

import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.inventory.PlayerInventory;
import com.github.wintersteve25.tau.components.utils.Container;
import com.github.wintersteve25.tau.components.utils.Padding;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.menu.TauContainerMenu;
import com.github.wintersteve25.tau.menu.UIMenu;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.Pad;
import com.github.wintersteve25.tau.utils.SimpleVec2i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoalGenGui implements UIMenu {
    @Override
    public UIComponent build(Layout layout, Theme theme, TauContainerMenu tauContainerMenu) {
        return new Container.Builder()
                .withChild(new Padding(new Pad.Builder().all(8).build(), new PlayerInventory()));
    }

    @Override
    public SimpleVec2i getSize() {
        return new SimpleVec2i(400, 400);
    }

    @Override
    public ItemStack quickMoveStack(TauContainerMenu menu, Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = menu.getSlot(index);
//        if (slot.hasItem()) {
//            ItemStack stack = slot.getItem();
//            itemstack = stack.copy();
//
//            int startPlayerInvIndex = getInvSize() + getModSlotAmount();
//            int startPlayerHBIndex = getInvSize() + getModSlotAmount() + 27;
//            int endPlayerInvIndex = slots.size();
//            int startMachineIndex = 0;
//            int startModSlotIndex = getInvSize();
//
//            if (slot instanceof ONIMachineSlotHandler || slot instanceof ONIModSlotHandler) {
//                if (!this.moveItemStackTo(stack, startPlayerInvIndex, endPlayerInvIndex, true)) {
//                    return ItemStack.EMPTY;
//                }
//                slot.onQuickCraft(stack, itemstack);
//            } else {
//                if (itemstack.getItem() instanceof ONIModificationItem) {
//                    if (!this.moveItemStackTo(stack, startModSlotIndex, startPlayerInvIndex, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                    slot.onQuickCraft(stack, itemstack);
//                }
//                if (index >= startPlayerHBIndex) {
//                    if (!this.moveItemStackTo(stack, startMachineIndex, startPlayerHBIndex, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                }
//                if (index >= startPlayerInvIndex && index < endPlayerInvIndex) {
//                    if (!this.moveItemStackTo(stack, startMachineIndex, startPlayerInvIndex, false)) {
//                        if (!this.moveItemStackTo(stack, startPlayerHBIndex, endPlayerInvIndex, false)) {
//                            return ItemStack.EMPTY;
//                        }
//                    }
//                }
//            }
//
//            if (stack.isEmpty()) {
//                slot.set(ItemStack.EMPTY);
//            } else {
//                slot.setChanged();
//            }
//
//            if (stack.getCount() == itemstack.getCount()) {
//                return ItemStack.EMPTY;
//            }
//
//            slot.onTake(player, stack);
//        }

        return itemstack;
    }
}
