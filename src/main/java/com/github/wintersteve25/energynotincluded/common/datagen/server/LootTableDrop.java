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
    
    public static LootTableDrop dropOther(ItemLike other) {
        return new LootTableDrop(LootTableBehaviour.OTHER, other);
    }
    
    public static LootTableDrop dontGenerate() {
        return new LootTableDrop(LootTableBehaviour.DONT_GENERATE, null);
    }

    public static LootTableDrop dropOtherWhenSilktouch(ItemLike other) {
        return new LootTableDrop(LootTableBehaviour.OTHER_SILKTOUCH, other);
    }
    
    public static LootTableDrop noDrop() {
        return new LootTableDrop(LootTableBehaviour.NO_DROP, null);
    }
}
