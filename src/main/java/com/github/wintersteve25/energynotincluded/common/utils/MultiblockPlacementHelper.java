package com.github.wintersteve25.energynotincluded.common.utils;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.BoundingShapeDefinition;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IPlacementCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.concurrent.atomic.AtomicBoolean;

public class MultiblockPlacementHelper {
    public static boolean isValidReplaceableBlock(Level level, BlockPlaceContext context, BlockPos pos) {
        if (!isBlockLoaded(level, pos)) {
            return false;
        }

        return level.getBlockState(pos).canBeReplaced(context);
    }

    public static boolean isBlockLoaded(Level level, BlockPos pos) {
        if (level == null) return false;
        if (!level.isInWorldBounds(pos)) {
            return false;
        }
        return level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
    }
    
    public static IPlacementCondition makeConditionFor(BoundingShapeDefinition block) {
        return (ctx, state) -> {
            Level level = ctx.getLevel();
            BlockPos pos = ctx.getClickedPos();
            AtomicBoolean result = new AtomicBoolean(true);
            
            block.foreachBoundingLocation(state.getValue(BlockStateProperties.FACING), pos, p -> {
                if (isValidReplaceableBlock(level, ctx, p)) {
                    return false;
                }
                
                result.set(false);
                return true;
            });
            
            return result.get();
        };
    }
}
