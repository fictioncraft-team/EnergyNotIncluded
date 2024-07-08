package com.github.wintersteve25.energynotincluded.common.datagen.client;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseDirectional;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIDirectionalBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ModelFileHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ResoureceLocationHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ONIStateProvider extends BlockStateProvider {

    public ONIStateProvider(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, ONIUtils.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        autoGenStatesAndModels();
    }

    private void autoGenStatesAndModels() {
        for (ONIBlockDeferredRegister.DeferredBlock<?, ?> b : ONIBlocks.BLOCKS.getAllBlocks().keySet()) {
            ONIBlockRegistryData data = ONIBlocks.BLOCKS.getAllBlocks().get(b);
            if (data.isDoStateGen()) {
                if (b.block().get() instanceof ONIBaseDirectional directional && data instanceof ONIDirectionalBlockRegistryData directionalData) {
                    directionalBlock(directional, directionalData.getModelFile(), directionalData.getAngleOffset());
                } else {
                    simpleBlock(b.block().get());
                }
            }
        }

        weightedRock(ONIBlocks.ABYSSALITE, 2);
        weightedRock(ONIBlocks.BLEACH_STONE, 8);
        weightedRock(ONIBlocks.FERTILIZER, 1);
        weightedRock(ONIBlocks.FOSSIL, 5);
        weightedRock(ONIBlocks.GOLD_AMALGAM, 8);
        weightedRock(ONIBlocks.GRANITE, 8);
        weightedRock(ONIBlocks.IGNEOUS_ROCK, 8);
        weightedRock(ONIBlocks.MAFIC_ROCK, 8);
        weightedRock(ONIBlocks.NEUTRONIUM, 8);
//        weightedRock(ONIBlocks.TileEntityBounded.OXYLITE_BLOCK, 8);
        weightedRock(ONIBlocks.PHOSPHORITE, 8);
        weightedRock(ONIBlocks.POLLUTED_ICE, 2);
        weightedRock(ONIBlocks.REGOLITH, 8);
        weightedRock(ONIBlocks.RUST, 1);
        weightedRock(ONIBlocks.WOLFRAMITE, 8);
    }

    private void weightedRock(ONIBlockDeferredRegister.DeferredBlock<?, ?> block, int amoutOfAlts) {
        String name = MiscHelper.langToReg(block.block().getId().getPath());
        weightedState(block.block().get(), name, new ResourceLocation(ONIUtils.MODID, "block/rocks/" + name), amoutOfAlts);
    }

    private void weightedState(Block block, String modelBaseName, ResourceLocation textureBaseLocation, int amountOfAlts) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int weight = 100 / amountOfAlts;

                    ConfiguredModel.Builder<?> builder = ConfiguredModel.builder()
                            .modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models()))
                            .weight(weight)
                            .nextModel();

                    for (int i = 1; i <= amountOfAlts; i++) {
                        builder.modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt" + i, ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models()))
                                .weight(weight);

                        if (i < amountOfAlts) {
                            builder.nextModel();
                        }
                    }

                    return builder.build();
                });
    }
}
