package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseBoundingMachine;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseDirectional;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.BoundingShapeDefinition;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ONIPlaceHolderBlock extends ONIBaseDirectional implements EntityBlock {
    public ONIPlaceHolderBlock() {
        this(Properties.of().strength(-3.5F, 3600000.0F).destroyTime(0).dynamicShape().noOcclusion());
    }

    public ONIPlaceHolderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        ONIPlaceHolderTE te = getTileEntityOrThrow(ONIPlaceHolderTE.class, level, pos);
        return te.addItem(player, hand, heldItem) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        ONIPlaceHolderTE te = getTileEntityOrThrow(ONIPlaceHolderTE.class, pLevel, pPos);
        if (te.getInPlaceOf() == null || te.getInPlaceOf().isEmpty()) {
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            return;
        }
        
        Block inPlace = te.getInPlaceOf().getBlock();

        if (inPlace instanceof ONIBaseBoundingMachine<?> boundingMachine) {
            BlockEntity tile = pLevel.getBlockEntity(pPos);
            Direction facing = pState.getValue(BlockStateProperties.FACING);
            ONIBaseBoundingMachine.removeBoundingMachine(pLevel, facing, pPos, boundingMachine);
        }

        if (te.getCompletionPercentage() >= 1) {
            if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
                pLevel.removeBlockEntity(pPos);
            }
            return;
        }
        
        pLevel.getServer()
                .getPlayerList()
                .broadcastSystemMessage(Component.translatable("oniutils.message.requests.buildCanceled", te.getInPlaceOf().getBlock().getName()), true);
        
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(ONIItems.BLUEPRINT.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        BlockEntity be = worldIn.getBlockEntity(pos);

        if (!(be instanceof ONIPlaceHolderTE te)) {
            return super.getShape(state, worldIn, pos, context);
        }

        if (te.getInPlaceOf() == null) {
            return super.getShape(state, worldIn, pos, context);
        }

        BlockState item = te.getInPlaceOf().getBlock().defaultBlockState();
        
        if (item.hasProperty(DirectionalBlock.FACING)) {
            item = item.setValue(DirectionalBlock.FACING, state.getValue(DirectionalBlock.FACING));
        }
        
        return item.getShape(worldIn, pos, context);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ONIBlocks.PLACEHOLDER_TE.get().create(pos, state);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}
