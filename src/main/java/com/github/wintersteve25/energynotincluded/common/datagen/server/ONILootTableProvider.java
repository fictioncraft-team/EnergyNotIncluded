package com.github.wintersteve25.energynotincluded.common.datagen.server;

import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;

public class ONILootTableProvider extends LootTableBase {
    public ONILootTableProvider(DataGenerator p_i50789_1_) {
        super(p_i50789_1_);
    }

    @Override
    protected void addTables() {
        standardTables();
    }

    private void standardTables() {
        for (ONIBlockDeferredRegister.DeferredBlock<?, ?> b : ONIBlocks.BLOCKS.getAllBlocks().keySet()) {
            ONIBlockRegistryData data = ONIBlocks.BLOCKS.getAllBlocks().get(b);
            if (data.isDoLootTableGen()) {
                Block block = b.block().get();
                lootTables.putIfAbsent(block, createStandardTable(b.block().getId().getPath(), block));
            }
        }
    }
}
