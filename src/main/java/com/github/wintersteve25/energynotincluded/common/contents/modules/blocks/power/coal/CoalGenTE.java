package com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal;

import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibItemRendererBase;
import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibModelBase;
import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseMachine;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.BoundingShapeDefinition;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.RelativeDir;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseAnimatedBlockItem;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.registries.ONIMenus;
import com.github.wintersteve25.energynotincluded.common.utils.MultiblockPlacementHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
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
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.*;
import com.github.wintersteve25.energynotincluded.common.data.plasma.api.EnumPlasmaTileType;
import com.github.wintersteve25.energynotincluded.common.data.plasma.api.Plasma;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseInvTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIBlockBuilder;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.utils.LangHelper;
import com.github.wintersteve25.energynotincluded.common.utils.MiscHelper;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static com.github.wintersteve25.energynotincluded.common.utils.MiscHelper.ONEPIXEL;

public class CoalGenTE extends ONIBaseInvTE implements ONIITickableServer, ONIIBoundingBlock, ONIIHasValidItems, GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Plasma plasmaHandler = new Plasma(4000, EnumPlasmaTileType.PRODUCER);
    private boolean removedFirstItem = false;

    private int progress = 0;
    private int totalProgress = ONIConfig.Server.COALGEN_PROCESS_TIME.getAsInt();

    public CoalGenTE(BlockPos pos, BlockState state) {
        super(ONIBlocks.COAL_GEN_TE.get(), pos, state);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (getLevel() == null) {
            return;
        }
        
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
                    sendNBTUpdatePacket();
                } else {
                    progress = 0;
                    sendNBTUpdatePacket();
                }
            }
        }

        if (!plasmaHandler.canGenerate(plasmaEachTick)) {
            removedFirstItem = false;
            progress = 0;
        }

        setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        plasmaHandler.deserializeNBT(provider, tag.getCompound("plasma"));
        progress = tag.getInt("progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("plasma", plasmaHandler.serializeNBT(provider));
        tag.putInt("progress", progress);
    }

    public int getProgress() {
        return progress;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setTotalProgress(int totalProgress) {
        this.totalProgress = totalProgress;
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
            controller.setAnimation(RawAnimation.begin().thenLoop("generating"));
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
    public BoundingShapeDefinition getDefinition() {
        return BOUNDING_SHAPE_DEFINITION;
    }

    private static final VoxelShape NORTH_R = Shapes.box(-1, 0, 0, 2, 3, 2);
    private static final BlockBehaviour.Properties PROPERTIES = BlockBehaviour.Properties.of().strength(1.4F, 5).requiresCorrectToolForDrops().noOcclusion();
    private static final BoundingShapeDefinition BOUNDING_SHAPE_DEFINITION = new BoundingShapeDefinition(
            new short[][][]{
                    {
                            {1, -1, 1},
                            {1, 1, 1},
                    },
                    {
                            {1, 1, 1},
                            {1, 1, 1}
                    },
                    {
                            {1, 1, 1},
                            {1, 1, 1}
                    }
            },
            new Vec3i(0, 0, 1)
    );

    public static final GeckolibModelBase<CoalGenTE> COAL_GEN_TE = new GeckolibModelBase<>("machines/power/coal_generator.geo.json", "machines/power/coal_generator.png", "machines/power/coal_generator.animation.json");
    public static final GeckolibModelBase<ONIBaseAnimatedBlockItem> COAL_GEN_IB = new GeckolibModelBase<>(COAL_GEN_TE);
    private static final Supplier<BlockEntityWithoutLevelRenderer> COAL_GEN_ISTER = () -> new GeckolibItemRendererBase<>(COAL_GEN_IB);

    public static final String ID = "coal_generator";

    public static ONIBlockBuilder<ONIBaseMachine<CoalGenTE>> createBlock() {
        return new ONIBlockBuilder<>(ID, () -> new ONIBaseMachine<CoalGenTE>(PROPERTIES, CoalGenTE.class, ONIBlocks.COAL_GEN_TE), COAL_GEN_ISTER)
                .placementCondition(MultiblockPlacementHelper.makeConditionFor(BOUNDING_SHAPE_DEFINITION))
                .renderType((state) -> RenderShape.ENTITYBLOCK_ANIMATED)
                .autoRotateShape()
                .shape((state, world, pos, ctx) -> NORTH_R)
                .container((world, pos) -> ONIMenus.COALGEN_UI)
                .setCategory(ONIItemCategory.POWER)
                .tooltip(LangHelper.itemTooltip(ID))
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
