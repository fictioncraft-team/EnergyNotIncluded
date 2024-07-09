package com.github.wintersteve25.energynotincluded.common.datagen.client;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseDirectional;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIDirectionalBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.MiscHelper;
import com.github.wintersteve25.energynotincluded.common.utils.ModelFileHelper;
import com.github.wintersteve25.energynotincluded.common.utils.ResoureceLocationHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
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
        for (Tuple<ONIBlockDeferredRegister.DeferredBlock<?, ?>, ONIBlockRegistryData> b : ONIBlocks.BLOCKS.getAllBlocks()) {
            if (b.getB().isDoStateGen()) {
                if (b.getA().block().get() instanceof ONIBaseDirectional directional && b.getB() instanceof ONIDirectionalBlockRegistryData directionalData) {
                    directionalBlock(directional, directionalData.getModelFile(), directionalData.getAngleOffset());
                } else {
                    simpleBlock(b.getA().block().get());
                }
            }
        }

        weightedRock(ONIBlocks.ABYSSALITE, 3);
        weightedRock(ONIBlocks.BLEACH_STONE, 9);
        weightedRock(ONIBlocks.FERTILIZER, 1);
        weightedRock(ONIBlocks.FOSSIL, 5);
        weightedRock(ONIBlocks.GOLD_AMALGAM, 8);
        weightedRock(ONIBlocks.GRANITE, 8);
        weightedRock(ONIBlocks.IGNEOUS_ROCK, 8);
        weightedRock(ONIBlocks.MAFIC_ROCK, 8);
        weightedRock(ONIBlocks.NEUTRONIUM, 8);
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

    private void weightedState(Block block, String modelBaseName, ResourceLocation textureBaseLocation, int textureCount) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int weight = 100 / textureCount;
                    
                    ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
                    for (int i = 1; i <= textureCount; i++) {
                        ResourceLocation tex = ResoureceLocationHelper.extend(textureBaseLocation, String.valueOf(i));
                        
                        builder.modelFile(ModelFileHelper.cubeAll(modelBaseName + i, tex, models()))
                                .weight(weight);

                        if (i < textureCount) {
                            builder = builder.nextModel();
                        }
                    }

                    return builder.build();
                });
    }
}
