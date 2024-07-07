package com.github.wintersteve25.energynotincluded.common.registration.block;

import com.github.wintersteve25.energynotincluded.common.datagen.server.LootTableDrop;
import com.github.wintersteve25.energynotincluded.common.registration.item.ONIItemRegistryData;

public class ONIBlockRegistryData extends ONIItemRegistryData {
    private final boolean doStateGen;
    private final LootTableDrop doLootTableGen;

    public ONIBlockRegistryData(boolean doStateGen, boolean doModelGen, boolean doLangGen, LootTableDrop doLootTableGen) {
        super(doModelGen, doLangGen);

        this.doStateGen = doStateGen;
        this.doLootTableGen = doLootTableGen;
    }

    public ONIBlockRegistryData() {
        this(false, true, true, LootTableDrop.dropSelf());
    }

    public boolean isDoStateGen() {
        return doStateGen;
    }

    public LootTableDrop lootTableBehaviour() {
        return doLootTableGen;
    }
}
