package com.github.wintersteve25.energynotincluded.common.contents.base.interfaces;

import com.github.wintersteve25.tau.menu.TauMenuHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseMachine;

/**
 * Should be implemented on a {@link ONIBaseMachine}
 * Or to be used in {@link com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIBlockBuilder#container(ONIIHasGui)}
 */
public interface ONIIHasGui {
    TauMenuHolder container(Level world, BlockPos pos);

    Component machineName();
}
