package com.github.wintersteve25.energynotincluded.common.datagen.server;

import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.Tuple;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ONILootTableProvider extends BlockLootSubProvider {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(ONILootTableProvider::new, LootContextParamSets.BLOCK)), provider);
    }

    protected ONILootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (Tuple<ONIBlockDeferredRegister.DeferredBlock<?, ?>, ONIBlockRegistryData> b : ONIBlocks.BLOCKS.getAllBlocks()) {
            LootTableDrop drop = b.getB().lootTableBehaviour();

            if (drop.behaviour() == LootTableBehaviour.DONT_GENERATE) {
                continue;
            }

            Block block = b.getA().block().get();

            switch (drop.behaviour()) {
                case SELF -> this.dropSelf(block);
                case SILKTOUCH -> this.dropWhenSilkTouch(block);
                case OTHER -> this.dropOther(block, Objects.requireNonNull(drop.other()));
                case OTHER_SILKTOUCH -> this.add(block, createSilkTouchOnlyTable(Objects.requireNonNull(drop.other())));
                case NO_DROP -> this.add(block, noDrop());
            }
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        Stream<Block> modBlocks = ONIBlocks.BLOCKS.getDeferredRegisterBlocks()
                .stream()
                .map(Holder::value);

        return modBlocks.toList();
    }
}
