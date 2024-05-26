package com.github.wintersteve25.energynotincluded.common.contents.base.interfaces;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface ONIIHasRedstoneOutput {

    int lowThreshold();

    int highThreshold();

    void setLowThreshold(int in);

    void setHighThreshold(int in);
}
