package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.BoundingShapeDefinition;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;

public class ONIBaseBoundingMachine<BE extends BlockEntity> extends ONIBaseMachine<BE> {

    private final BoundingShapeDefinition shapeDefinition;

    public ONIBaseBoundingMachine(Properties properties, Class<BE> beClass, DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> blockEntityType, BoundingShapeDefinition shapeDefinition) {
        super(properties, beClass, blockEntityType);
        this.shapeDefinition = shapeDefinition;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!(worldIn.getBlockEntity(pos) instanceof ONIBaseTE tile)) {
            return;
        }

        Direction facing = state.getValue(BlockStateProperties.FACING);
        buildBoundingMachine(worldIn, facing, pos, this);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            BlockEntity tile = level.getBlockEntity(pos);
            Direction facing = state.getValue(BlockStateProperties.FACING);
            removeBoundingMachine(level, facing, pos, this);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public BoundingShapeDefinition getShapeDefinition(Level level, BlockPos pos) {
        return shapeDefinition;
    }

    /**
     * Method modified from <a href="https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/util/WorldUtils.java#L537">...</a>
     */
    private static void makeBoundingBlock(@Nullable LevelAccessor world, BlockPos boundingLocation, BlockPos orig) {
        if (world == null) {
            return;
        }

        ONIBoundingBlock boundingBlock = ONIBlocks.BOUNDING_BLOCK.block().get();
        BlockState newState = boundingBlock.defaultBlockState();
        world.setBlock(boundingLocation, newState, 3);

        if (world.isClientSide()) return;
        if (world.getBlockEntity(boundingLocation) instanceof ONIBoundingTE tile) {
            tile.setMainLocation(orig);
        } else {
            ONIUtils.LOGGER.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
        }
    }
    
    public static void buildBoundingMachine(Level level, Direction facing, BlockPos controllerPos, ONIBaseBoundingMachine<?> boundingMachine) {
        boundingMachine.getShapeDefinition(level, controllerPos).foreachBoundingLocation(facing, controllerPos, (p) -> {
            makeBoundingBlock(level, p, controllerPos);
            return false;
        });
    }
    
    public static void removeBoundingMachine(Level level, Direction facing, BlockPos controllerPos, ONIBaseBoundingMachine<?> boundingMachine) {
        boundingMachine.getShapeDefinition(level, controllerPos).foreachBoundingLocation(facing, controllerPos, (p) -> {
            level.removeBlock(p, false);
            return false;
        });
    }
}
