package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIForceStoppable;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IRenderTypeProvider;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IVoxelShapeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ONIBaseBlock extends Block implements SimpleWaterloggedBlock {

    // block builder properties
    private IVoxelShapeProvider hitBox;
    private IRenderTypeProvider renderType;

    public ONIBaseBlock(int harvestLevel, float destroyTime, float explosionRes) {
        this(harvestLevel, destroyTime, explosionRes, SoundType.STONE);
    }

    public ONIBaseBlock(int harvestLevel, float destroyTime, float explosionRes, SoundType soundType) {
        this(Properties.of().strength(destroyTime, explosionRes).sound(soundType));
    }

    public ONIBaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getHitBox() == null ? super.getShape(state, worldIn, pos, context) : getHitBox().createShape(state, worldIn, pos, context);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return getRenderType() == null ? super.getRenderShape(state) : getRenderType().createRenderType(state);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!(worldIn.getBlockEntity(pos) instanceof ONIBaseTE tile)) {
            return;
        }

        if (tile instanceof ONIIBoundingBlock block) {
            block.onPlace();
        }

        if (tile instanceof ONIIForceStoppable forceStoppable) {
            if (forceStoppable.isInverted()) {
                forceStoppable.setForceStopped(!worldIn.hasNeighborSignal(pos));
            } else {
                forceStoppable.setForceStopped(worldIn.hasNeighborSignal(pos));
            }
        }

        tile.onPlacedBy(worldIn, pos, state, placer, stack);
    }

    public IVoxelShapeProvider getHitBox() {
        return hitBox;
    }

    public void setHitBox(IVoxelShapeProvider hitBox) {
        this.hitBox = hitBox;
    }

    public IRenderTypeProvider getRenderType() {
        return renderType;
    }

    public void setRenderType(IRenderTypeProvider renderType) {
        this.renderType = renderType;
    }

    protected <T extends BlockEntity> T getTileEntityOrThrow(Class<T> clazz, BlockGetter accessor, BlockPos pos) {
        BlockEntity blockEntity = accessor.getBlockEntity(pos);

        if (blockEntity == null || !clazz.isAssignableFrom(blockEntity.getClass())) {
            throw new IllegalStateException("Expected tile entity of type at " + pos + " but none was found");
        }

        return (T) blockEntity;
    }
}
