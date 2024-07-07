package com.github.wintersteve25.energynotincluded.common.datagen.server;

import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;

public record LootTableDrop(LootTableBehaviour behaviour, @Nullable ItemLike other) {
    public static LootTableDrop dropSelf() {
        return new LootTableDrop(LootTableBehaviour.SELF, null);
    }
    
    public static LootTableDrop dropWhenSilktouched() {
        return new LootTableDrop(LootTableBehaviour.SILKTOUCH, null);
    }
    
    public static LootTableDrop dropOther(ItemLike itemLike) {
        return new LootTableDrop(LootTableBehaviour.OTHER, itemLike);
    }
    
    public static LootTableDrop dontGenerate() {
        return new LootTableDrop(LootTableBehaviour.DONT_GENERATE, null);
    }
}
