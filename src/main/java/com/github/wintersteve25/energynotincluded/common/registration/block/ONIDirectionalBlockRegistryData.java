package com.github.wintersteve25.energynotincluded.common.registration.block;

import com.github.wintersteve25.energynotincluded.common.datagen.server.LootTableDrop;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class ONIDirectionalBlockRegistryData extends ONIBlockRegistryData {
    private final int angleOffset;
    private final ModelFile modelFile;

    public ONIDirectionalBlockRegistryData(boolean doStateGen, boolean doModelGen, boolean doLangGen, LootTableDrop lootTable, boolean includeInCreativeTab, int angleOffset, ModelFile modelFile) {
        super(doStateGen, doModelGen, doLangGen, lootTable, includeInCreativeTab);
        this.angleOffset = angleOffset;
        this.modelFile = modelFile;
    }

    public ONIDirectionalBlockRegistryData(int angleOffset, ModelFile modelFile) {
        this(false, true, true, LootTableDrop.dropSelf(), true, angleOffset, modelFile);
    }

    public ONIDirectionalBlockRegistryData() {
        this.angleOffset = 0;
        this.modelFile = null;
    }

    public int getAngleOffset() {
        return angleOffset;
    }

    public ModelFile getModelFile() {
        return modelFile;
    }
}
