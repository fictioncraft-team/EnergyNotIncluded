package com.github.wintersteve25.energynotincluded.common.contents.base.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ONIITickableClient {
    void clientTick(Level level, BlockPos blockPos, BlockState blockState);
}
