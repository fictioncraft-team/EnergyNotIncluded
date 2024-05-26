package com.github.wintersteve25.energynotincluded.common.datagen.client;

import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseDirectional;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIDirectionalBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ModelFileHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ResoureceLocationHelper;

public class ONIStateProvider extends BlockStateProvider {

    public ONIStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ONIUtils.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        autoGenStatesAndModels();
    }

    private void autoGenStatesAndModels() {
        for (BlockRegistryObject<? extends Block, ? extends BlockItem> b : ONIBlocks.BLOCKS.getAllBlocks().keySet()) {
            ONIBlockRegistryData data = ONIBlocks.BLOCKS.getAllBlocks().get(b);
            if (data.isDoStateGen()) {
                if (b.getBlock() instanceof ONIBaseDirectional directional && data instanceof ONIDirectionalBlockRegistryData directionalData) {
                    directionalBlock(directional, directionalData.getModelFile(), directionalData.getAngleOffset());
                } else {
                    simpleBlock(b.getBlock());
                }
            }
        }

        weightedRock(ONIBlocks.NonFunctionals.ABYSSALITE, 2);
        weightedRock(ONIBlocks.NonFunctionals.BLEACH_STONE, 8);
        weightedRock(ONIBlocks.NonFunctionals.FERTILIZER, 1);
        weightedRock(ONIBlocks.NonFunctionals.FOSSIL, 5);
        weightedRock(ONIBlocks.NonFunctionals.GOLD_AMALGAM, 8);
        weightedRock(ONIBlocks.NonFunctionals.GRANITE, 8);
        weightedRock(ONIBlocks.NonFunctionals.IGNEOUS_ROCK, 8);
        weightedRock(ONIBlocks.NonFunctionals.MAFIC_ROCK, 8);
        weightedRock(ONIBlocks.NonFunctionals.NEUTRONIUM, 8);
//        weightedRock(ONIBlocks.TileEntityBounded.OXYLITE_BLOCK, 8);
        weightedRock(ONIBlocks.NonFunctionals.PHOSPHORITE, 8);
        weightedRock(ONIBlocks.NonFunctionals.POLLUTED_ICE, 2);
        weightedRock(ONIBlocks.NonFunctionals.REGOLITH, 8);
        weightedRock(ONIBlocks.NonFunctionals.RUST, 1);
        weightedRock(ONIBlocks.NonFunctionals.WOLFRAMITE, 8);
    }

    private void weightedRock(BlockRegistryObject<? extends Block, BlockItem> block, int amoutOfAlts) {
        String name = MiscHelper.langToReg(block.getName());
        weightedState(block.getBlock(), name, new ResourceLocation(ONIUtils.MODID, "block/rocks/" + name), amoutOfAlts);
    }

    private void weightedState(Block block, String modelBaseName, ResourceLocation textureBaseLocation, int amountOfAlts) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    switch (amountOfAlts) {
                        case 1:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).build();
                        case 2:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).build();
                        case 3:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).build();
                        case 4:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).build();
                        case 5:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).build();
                        case 6:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResoureceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).build();
                        case 7:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResoureceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResoureceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).build();
                        case 8:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResoureceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResoureceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResoureceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).build();
                        case 9:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResoureceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResoureceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResoureceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResoureceLocationHelper.extend(textureBaseLocation, "_alt9"), models())).weight((int) 100 / amountOfAlts).build();
                        case 10:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResoureceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResoureceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResoureceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResoureceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResoureceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResoureceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResoureceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResoureceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResoureceLocationHelper.extend(textureBaseLocation, "_alt9"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResoureceLocationHelper.extend(textureBaseLocation, "_alt10"), models())).weight((int) 100 / amountOfAlts).build();
                        default:
                            ONIUtils.LOGGER.warn("Tried to create weighted state out of supported range");
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).build();
                    }
                });
    }
}
