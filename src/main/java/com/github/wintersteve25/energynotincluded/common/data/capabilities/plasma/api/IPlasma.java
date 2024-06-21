package com.github.wintersteve25.energynotincluded.common.data.capabilities.plasma.api;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IPlasma extends INBTSerializable<CompoundTag> {
    void addPower(int power);

    void removePower(int power);

    int getPower();

    void setPower(int power);

    boolean canGenerate(int power);

    boolean canExtract(int power);

    void setCapacity(int capacity);

    int getCapacity();

    EnumPlasmaTileType getTileType();

    void setTileType(EnumPlasmaTileType type);
}
