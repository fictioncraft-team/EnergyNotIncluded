package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding;

import com.github.wintersteve25.energynotincluded.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Function;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface ONIIBoundingBlock {
    BoundingShapeDefinition getDefinition();
}

