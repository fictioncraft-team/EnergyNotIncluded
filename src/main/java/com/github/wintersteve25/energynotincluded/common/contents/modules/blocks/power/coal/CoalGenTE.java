package com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal;

import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseMachine;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.registries.ONIMenus;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.joml.Vector3d;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.*;
import com.github.wintersteve25.energynotincluded.common.data.plasma.api.EnumPlasmaTileType;
import com.github.wintersteve25.energynotincluded.common.data.plasma.api.Plasma;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseInvTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIBlockBuilder;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.LangHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.BiPredicate;

import static com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper.ONEPIXEL;

public class CoalGenTE extends ONIBaseInvTE implements ONIITickableServer, ONIIBoundingBlock, ONIIHasValidItems, GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Plasma plasmaHandler = new Plasma(4000, EnumPlasmaTileType.PRODUCER);
    private boolean removedFirstItem = false;

    private int progress = 0;
    private final int totalProgress = ONIConfig.Server.COALGEN_PROCESS_TIME.getAsInt();

    public CoalGenTE(BlockPos pos, BlockState state) {
        super(ONIBlocks.COAL_GEN_TE.get(), pos, state);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (getLevel() != null) {
            int plasmaEachTick = 10;

            if (progress > 0) {
                if (plasmaHandler.canGenerate(plasmaEachTick)) {
                    if (!removedFirstItem) {
                        itemHandler.extractItem(0, 1, false);
                        removedFirstItem = true;
                    }
                    progress -= 1;
                    plasmaHandler.addPower(plasmaEachTick);
                    if (progress <= 0) {
                        removedFirstItem = false;
                        progress = 0;
                    }
                }
            } else {
                if (plasmaHandler.canGenerate(plasmaEachTick)) {
                    if (!itemHandler.getStackInSlot(0).isEmpty()) {
                        progress = totalProgress;
                    } else {
                        progress = 0;
                    }
                }
            }

            if (!plasmaHandler.canGenerate(plasmaEachTick)) {
                removedFirstItem = false;
                progress = 0;
            }

            setChanged();
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        plasmaHandler.deserializeNBT(provider, tag.getCompound("plasma"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("plasma", plasmaHandler.serializeNBT(provider));
    }

    @Override
    public int getInvSize() {
        return 1;
    }

    @Override
    public BiPredicate<ItemStack, Integer> validItemsPredicate() {
        return (stack, slot) -> stack.getItem() == Items.COAL;
    }

    private PlayState animationPredicate(AnimationState<CoalGenTE> state) {
        AnimationController<CoalGenTE> controller = state.getController();
        controller.transitionLength(80);
        
        if (progress > 0) {
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.motor.new"));
            return PlayState.CONTINUE;
        } else {
            controller.setAnimation(RawAnimation.begin());
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

    @Override
    public void onPlace() {
        Direction facing = getBlockState().getValue(BlockStateProperties.FACING);

        switch (facing) {
            case NORTH:
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().east(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above().east(), this.getBlockPos());
                break;
            case SOUTH:
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().west(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above().west(), this.getBlockPos());
                break;
            case EAST:
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().south(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above().south(), this.getBlockPos());
                break;
            case WEST:
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().north(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above(), this.getBlockPos());
                MiscHelper.makeBoundingBlock(this.getLevel(), this.getBlockPos().above().north(), this.getBlockPos());
                break;
        }
    }

    @Override
    public void onBreak(BlockState oldState) {
        if (this.level != null) {
            Direction facing = getBlockState().getValue(BlockStateProperties.FACING);

            switch (facing) {
                case NORTH:
                    this.level.removeBlock(this.getBlockPos().east(), false);
                    this.level.removeBlock(this.getBlockPos().above(), false);
                    this.level.removeBlock(this.getBlockPos().above().east(), false);
                    break;
                case SOUTH:
                    this.level.removeBlock(this.getBlockPos().west(), false);
                    this.level.removeBlock(this.getBlockPos().above(), false);
                    this.level.removeBlock(this.getBlockPos().above().west(), false);
                    break;
                case EAST:
                    this.level.removeBlock(this.getBlockPos().south(), false);
                    this.level.removeBlock(this.getBlockPos().above(), false);
                    this.level.removeBlock(this.getBlockPos().above().south(), false);
                    break;
                case WEST:
                    this.level.removeBlock(this.getBlockPos().north(), false);
                    this.level.removeBlock(this.getBlockPos().above(), false);
                    this.level.removeBlock(this.getBlockPos().above().north(), false);
                    break;
            }
        }
    }

    private static final VoxelShape BOTTOM1 = Shapes.box(0D, 0, 0D, 1D, ONEPIXEL + (ONEPIXEL / 16) * 2, 1D);
    private static final VoxelShape BOTTOM2 = Shapes.box(1D, 0, 0D, 2D, ONEPIXEL + (ONEPIXEL / 16) * 2, 1D);
    private static final VoxelShape BOTTOM = Shapes.or(BOTTOM1, BOTTOM2);
    private static final VoxelShape SUPPORT1 = Shapes.box(ONEPIXEL * 6, ONEPIXEL + (ONEPIXEL / 16) * 2, ONEPIXEL * 6, ONEPIXEL * 6, 1 + ONEPIXEL * 10, ONEPIXEL * 11);
    private static final VoxelShape SUPPORT2 = Shapes.box(2D - ONEPIXEL * 3, ONEPIXEL + (ONEPIXEL / 16) * 2, ONEPIXEL * 6, 2D - ONEPIXEL * 1, 1 + ONEPIXEL * 10, ONEPIXEL * 11);
    private static final VoxelShape SUPPORT = Shapes.or(SUPPORT1, SUPPORT2);
    private static final VoxelShape MIDDLE = Shapes.box(ONEPIXEL * 6, ONEPIXEL * 6, ONEPIXEL * 4, 2D - ONEPIXEL * 3, 1 + ONEPIXEL * 9, 1D - ONEPIXEL * 4);
    private static final VoxelShape REDSTONEPANEL = MiscHelper.rotate(Shapes.box(ONEPIXEL * 4, ONEPIXEL, ONEPIXEL * 14, ONEPIXEL * 13, ONEPIXEL * 13, 1D), Rotation.CLOCKWISE_90);
    private static final VoxelShape CONNECTION = MiscHelper.rotate(Shapes.box(ONEPIXEL * 7, ONEPIXEL * 7, ONEPIXEL * 12, ONEPIXEL * 10, ONEPIXEL * 11, ONEPIXEL * 14), Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape NORTH_R = Shapes.or(BOTTOM, SUPPORT, MIDDLE, CONNECTION, REDSTONEPANEL).optimize();
    private static final BlockBehaviour.Properties PROPERTIES = BlockBehaviour.Properties.of().strength(1.4F, 5).requiresCorrectToolForDrops().noOcclusion();

    public static ONIBlockBuilder<ONIBaseMachine<CoalGenTE>> createBlock() {
        return new ONIBlockBuilder<ONIBaseMachine<CoalGenTE>>(ONIConstants.LangKeys.COAL_GEN, () -> new ONIBaseMachine<CoalGenTE>(PROPERTIES, CoalGenTE.class, ONIBlocks.COAL_GEN_TE), ONIConstants.Geo.COAL_GEN_ISTER)
                .placementCondition(ONIConstants.PlacementConditions::fourByFourCondition)
                .renderType((state) -> RenderShape.ENTITYBLOCK_ANIMATED)
                .autoRotateShape()
                .shape((state, world, pos, ctx) -> NORTH_R)
                .container((world, pos) -> ONIMenus.COALGEN_UI)
                .setCategory(ONIItemCategory.POWER)
                .tooltip(LangHelper.itemTooltip(ONIConstants.LangKeys.COAL_GEN))
                .shiftToolTip()
                .noModelGen();
    }
    
    public static void registerCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<CoalGenTE> type) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (coalgen, dir) -> {
            return coalgen.itemHandler;
        });
        
        event.registerBlockEntity(ONICapabilities.PLASMA, type, (coalgen, dir) -> {
            return coalgen.plasmaHandler;
        });
    }
}
