package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseMachine;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder.ONIPlaceHolderBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder.ONIPlaceHolderTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItemBlock;
import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import com.github.wintersteve25.energynotincluded.common.datagen.server.LootTableDrop;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockEntityDeferredRegister;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseDirectional;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIBlockBuilder;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIDirectionalBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.utils.ModelFileHelper;
import com.github.wintersteve25.energynotincluded.common.utils.ResoureceLocationHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

public class ONIBlocks {

    public static final ONIBlockDeferredRegister BLOCKS = new ONIBlockDeferredRegister(ONIUtils.MODID);
    public static final ONIBlockEntityDeferredRegister BLOCK_ENTITIES = new ONIBlockEntityDeferredRegister(ONIUtils.MODID);

    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> IGNEOUS_ROCK = BLOCKS.register("igneous_rock", () -> new ONIBaseBlock(1, 2, 6));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseDirectional, ONIBaseItemBlock> SEDIMENTARY_ROCK = BLOCKS.register("sedimentary_rock", () -> new ONIBaseDirectional(0, 4, 10), new ONIDirectionalBlockRegistryData(0, ModelFileHelper.createModelFile(ResoureceLocationHelper.ResourceLocationBuilder
            .getBuilder()
            .block()
            .rocks()
            .addPath("sedimentary_rock")
            .build())));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> MAFIC_ROCK = BLOCKS.register("mafic_rock", () -> new ONIBaseBlock(2, 5, 18));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> REGOLITH = BLOCKS.register("regolith", () -> new ONIBaseBlock(1, 5, 18));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> NEUTRONIUM = BLOCKS.register("neutronium", () -> new ONIBaseBlock(3, 7, 30));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> FOSSIL = BLOCKS.register("fossil", () -> new ONIBaseBlock(0, 2, 3));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> BLEACH_STONE = BLOCKS.register("bleach_stone", () -> new ONIBaseBlock(1, 1.5F, 2));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> RUST = BLOCKS.register("rust", () -> new ONIBaseBlock(1, 1.5F, 2));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> POLLUTED_ICE = BLOCKS.register("polluted_ice", () -> new ONIBaseBlock(BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(0.7F, 1).friction(0.98F)));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> ALGAE = BLOCKS.register("algae", () -> new ONIBaseBlock(BlockBehaviour.Properties.of().sound(SoundType.CROP).strength(0.2F, 1)), true, true, true, LootTableDrop.dropSelf(), true);
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> PHOSPHORITE = BLOCKS.register("phosphorite", () -> new ONIBaseBlock(1, 1, 2));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> FERTILIZER = BLOCKS.register("fertilizer", () -> new ONIBaseBlock(BlockBehaviour.Properties.of().sound(SoundType.CROP).strength(0.2F, 1)));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> GOLD_AMALGAM = BLOCKS.register("gold_amalgam", () -> new ONIBaseBlock(2, 5, 5));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> WOLFRAMITE = BLOCKS.register("wolframite", () -> new ONIBaseBlock(2, 6, 20));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> ABYSSALITE = BLOCKS.register("abyssalite", () -> new ONIBaseBlock(2, 5, 15));
    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseBlock, ONIBaseItemBlock> GRANITE = BLOCKS.register("granite", () -> new ONIBaseBlock(2, 4, 12));

    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBaseMachine<CoalGenTE>, BlockItem> COAL_GEN_BLOCK = registerBuilder(CoalGenTE.createBlock());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoalGenTE>> COAL_GEN_TE = BLOCK_ENTITIES.register(COAL_GEN_BLOCK, CoalGenTE::new, CoalGenTE::registerCapabilities);

    public static final ONIBlockDeferredRegister.DeferredBlock<ONIBoundingBlock, ONIBaseItemBlock> BOUNDING_BLOCK = BLOCKS.register("bounding_block", ONIBoundingBlock::new, false, false, false, LootTableDrop.noDrop(), false);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ONIBoundingTE>> BOUNDING_TE = BLOCK_ENTITIES.register(BOUNDING_BLOCK, ONIBoundingTE::new);

    public static final ONIBlockDeferredRegister.DeferredBlock<ONIPlaceHolderBlock, ONIBaseItemBlock> PLACEHOLDER_BLOCK = BLOCKS.register("placeholder", ONIPlaceHolderBlock::new, false, false, false, LootTableDrop.noDrop(), false);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ONIPlaceHolderTE>> PLACEHOLDER_TE = BLOCK_ENTITIES.register(PLACEHOLDER_BLOCK, ONIPlaceHolderTE::new);

    private static <T extends ONIBaseBlock> ONIBlockDeferredRegister.DeferredBlock<T, BlockItem> registerBuilder(ONIBlockBuilder<T> builder) {
        Tuple<Lazy<T>, Function<ONIBaseBlock, ONIBaseItemBlock>> build = builder.build();
        return BLOCKS.register(builder.getRegName(), () -> build.getA().get(), (b) -> build.getB().apply(b), builder.isDoStateGen(), builder.isDoModelGen(), builder.isDoLangGen(), builder.getLootTableDrop(), builder.isIncludeInCreativeTab());
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}
