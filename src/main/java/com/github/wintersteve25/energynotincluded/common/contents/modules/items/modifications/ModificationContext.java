package com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseInvTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.enums.EnumModifications;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ONIModInventoryHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModificationContext {

    private final ONIBaseInvTE parent;

    private final ONIModInventoryHandler modHandler;
    private final int maxModAmount;
    private final EnumModifications[] validMods;

    private final ONICombinedInvWrapper combinedInventory;
    private final LazyOptional<IItemHandler> combinedLazyOptional;

    public ModificationContext(ONIBaseInvTE parent, int maxModAmount, EnumModifications... validMods) {
        this.parent = parent;
        this.maxModAmount = maxModAmount;
        this.validMods = validMods;
        this.modHandler = new ONIModInventoryHandler(this);

        this.combinedInventory = new ONICombinedInvWrapper(parent.getItemHandler(), modHandler);
        this.combinedLazyOptional = LazyOptional.of(() -> combinedInventory);
    }

    public void deserializeNBT(CompoundTag nbt) {
        modHandler.deserializeNBT(nbt);
    }

    public CompoundTag serializeNBT() {
        return modHandler.serializeNBT();
    }

    public ONIBaseInvTE getParent() {
        return parent;
    }

    public int getMaxModAmount() {
        return maxModAmount;
    }

    public LazyOptional<IItemHandler> getCombinedLazyOptional() {
        return combinedLazyOptional;
    }

    public EnumModifications[] getValidMods() {
        return validMods;
    }

    public boolean isModValid(EnumModifications mod) {
        return Arrays.asList(getValidMods()).contains(mod);
    }

    public ONIModInventoryHandler getModHandler() {
        return modHandler;
    }

    public List<ItemStack> getInstalledMods() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < getMaxModAmount(); i++) {
            ItemStack mod = getModHandler().getStackInSlot(i);
            if (!mod.isEmpty()) {
                list.add(mod);
            }
        }
        return list;
    }

    public boolean containsUpgrades() {
        for (int i = 0; i < getMaxModAmount(); i++) {
            if (!modHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static class ONICombinedInvWrapper extends CombinedInvWrapper {
        public ONICombinedInvWrapper(IItemHandlerModifiable... itemHandler) {
            super(itemHandler);
        }

        @Override
        public IItemHandlerModifiable getHandlerFromIndex(int index) {
            if (index < 0 || index >= itemHandler.length)
            {
                return (IItemHandlerModifiable) EmptyHandler.INSTANCE;
            }
            return itemHandler[index];
        }
    }
}
