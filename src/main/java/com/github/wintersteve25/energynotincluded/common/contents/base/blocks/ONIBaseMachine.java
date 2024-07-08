package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIHasGui;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ISHandlerHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class ONIBaseMachine<BE extends BlockEntity> extends ONIBaseDirectional implements EntityBlock {

    // block builder properties
    private ONIIHasGui gui;
    private final Class<BE> beClass;
    private final DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> blockEntityType;

    public ONIBaseMachine(Properties properties, Class<BE> beClass, DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> blockEntityType) {
        super(properties);
        this.beClass = beClass;
        this.blockEntityType = blockEntityType;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult pHitResult) {
        if (world.isClientSide()) {
            return super.useWithoutItem(state, world, pos, player, pHitResult);
        }

        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (!isCorrectTe(tileEntity)) {
            return super.useWithoutItem(state, world, pos, player, pHitResult);
        }

        if (gui != null || this instanceof ONIIHasGui) {
            if (gui == null) {
                gui = (ONIIHasGui) this;
            }
            
            gui.container(world, pos).openMenu((ServerPlayer) player, pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (isCorrectTe(tile) && tile instanceof ONIBaseTE) {
            return ((ONIBaseTE) tile).canConnectRedstone(state, world, pos, side);
        }
        return super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (isCorrectTe(world.getBlockEntity(pos))) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof ONIBaseTE baseTE) {
                if (!(baseTE instanceof ONIBaseInvTE te)) {
                    return super.playerWillDestroy(world, pos, state, player);
                }

                if (te.hasItem()) {
                    ISHandlerHelper.dropInventory(te, world, state, pos, te.getInvSize());
                }
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof ONIIBoundingBlock) {
                ((ONIIBoundingBlock) tile).onBreak(state);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntityType.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return ONIBaseTE::clientTicker;
        }
        return ONIBaseTE::serverTicker;
    }

    public boolean isCorrectTe(BlockEntity tile) {
        return getBeClass() != null && getBeClass().isInstance(tile);
    }

    public Class<? extends BlockEntity> getBeClass() {
        return beClass;
    }

    @Nullable
    public ONIIHasGui getGui() {
        return gui == null ? this instanceof ONIIHasGui ? (ONIIHasGui) this : null : gui;
    }

    public void setGui(ONIIHasGui gui) {
        this.gui = gui;
    }
}
