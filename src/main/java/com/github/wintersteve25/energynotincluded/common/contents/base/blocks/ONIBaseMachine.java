package com.github.wintersteve25.energynotincluded.common.contents.base.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
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
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIHasRedstoneOutput;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.ONIIModifiable;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications.ONIModificationItem;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ISHandlerHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.LangHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class ONIBaseMachine<BE extends BlockEntity> extends ONIBaseDirectional implements EntityBlock {

    // block builder properties
    private ONIIHasGui gui;
    private final Class<BE> beClass;
    private final DeferredHolder<BlockEntityType<BE>, BlockEntityType<BE>> blockEntityType;

    public ONIBaseMachine(Properties properties, Class<BE> beClass, DeferredHolder<BlockEntityType<BE>, BlockEntityType<BE>> blockEntityType) {
        super(properties);
        this.beClass = beClass;
        this.blockEntityType = blockEntityType;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult) {
        ItemStack heldItem = player.getItemInHand(pHand);
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (!isCorrectTe(tileEntity)) {
            ONIUtils.LOGGER.warn("Wrong tileEntity type found, failed to create container");
            return super.useItemOn(stack, state, world, pos, player, pHand, pHitResult);
        }

        if (tileEntity instanceof ONIIModifiable && tileEntity instanceof ONIBaseTE baseTE) {
            baseTE.onBlockActivated(state, world, pos, player, pHand, pHitResult);
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ONIModificationItem) {
                ONIIModifiable modifiable = (ONIIModifiable) tileEntity;
                if (modifiable.addMod((ONIBaseTE) tileEntity, heldItem)) {
                    player.swing(pHand, true);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return super.useItemOn(stack, state, world, pos, player, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult pHitResult) {
        if (world.isClientSide()) {
            return super.useWithoutItem(state, world, pos, player, pHitResult);
        }

        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (!isCorrectTe(tileEntity)) {
            ONIUtils.LOGGER.warn("Wrong tileEntity type found, failed to create container");
            return super.useWithoutItem(state, world, pos, player, pHitResult);
        }

        if (gui != null || this instanceof ONIIHasGui) {
            if (gui == null) {
                gui = (ONIIHasGui) this;
            }
            if (gui.machineName() != null) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return gui.machineName();
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int cid, Inventory playerInventory, Player playerEntity) {
                        return gui.container(world, pos).newInstance(playerInventory, cid);
                    }
                };

                player.openMenu(containerProvider);
            }
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
                baseTE.onHarvested(world, pos, state, player);
                if (baseTE instanceof ONIBaseInvTE) {
                    ONIBaseInvTE te = (ONIBaseInvTE) world.getBlockEntity(pos);
                    if (te != null) {
                        if (te.hasItem()) {
                            ISHandlerHelper.dropInventory(te, world, state, pos, te.getInvSize());
                        }

                        if (te instanceof ONIIModifiable modifiable) {
                            if (modifiable.modContext().containsUpgrades()) {
                                ISHandlerHelper.dropInventory(modifiable.modContext().getModHandler(), world, state, pos, modifiable.modContext().getMaxModAmount());
                            }
                        }
                    }
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
            if (isCorrectTe(tile) && tile instanceof ONIBaseTE) {
                ((ONIBaseTE) tile).onBroken(state, world, pos, newState, isMoving);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        BlockEntity tile = blockAccess.getBlockEntity(pos);
        if (tile instanceof ONIIHasRedstoneOutput redstoneOutput) {
            AtomicBoolean isLowTrue = new AtomicBoolean(false);
            AtomicBoolean isHighTrue = new AtomicBoolean(false);

            tile.getCapabilities(ONICapabilities.PLASMA).ifPresent(power -> {
                if ((power.getPower() / power.getCapacity()) * 100 < redstoneOutput.lowThreshold()) {
                    isLowTrue.set(true);
                }

                if ((power.getPower() / power.getCapacity()) * 100 > redstoneOutput.highThreshold()) {
                    isHighTrue.set(true);
                }
            });

            return isHighTrue.get() || isLowTrue.get() ? 15 : 0;
        }
        if (isCorrectTe(tile) && tile instanceof ONIBaseTE) {
            ((ONIBaseTE) tile).getWeakPower(blockState, blockAccess, pos, side);
        }

        return 0;
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
