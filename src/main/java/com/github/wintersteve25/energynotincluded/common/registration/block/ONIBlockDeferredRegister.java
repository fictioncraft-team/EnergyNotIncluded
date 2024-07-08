package com.github.wintersteve25.energynotincluded.common.registration.block;

import com.github.wintersteve25.energynotincluded.common.datagen.server.LootTableDrop;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItemBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ONIBlockDeferredRegister {

    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> blockItems;
    private final List<Tuple<DeferredBlock<?, ?>, ONIBlockRegistryData>> allBlocks = new ArrayList<>();

    public ONIBlockDeferredRegister(String modid) {
        this.blocks = DeferredRegister.create(Registries.BLOCK, modid);
        this.blockItems = DeferredRegister.create(Registries.ITEM, modid);
    }

    public <B extends Block> DeferredBlock<B, ONIBaseItemBlock> register(String name, Supplier<B> blockSupplier) {
        return registerDefaultProperties(name, blockSupplier, ONIBaseItemBlock::new);
    }

    public <B extends Block> DeferredBlock<B, ONIBaseItemBlock> register(String name, Supplier<B> blockSupplier, boolean doStateGen, boolean doModelGen, boolean doLangGen, LootTableDrop lootTable) {
        return registerDefaultProperties(name, blockSupplier, ONIBaseItemBlock::new, doStateGen, doModelGen, doLangGen, lootTable);
    }

    public <B extends Block> DeferredBlock<B, ONIBaseItemBlock> register(String name, Supplier<B> blockSupplier, ONIBlockRegistryData data) {
        return registerDefaultProperties(name, blockSupplier, ONIBaseItemBlock::new, data);
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> registerDefaultProperties(String name, Supplier<B> blockSupplier, BiFunction<B, Item.Properties, I> itemCreator) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, ONIUtils.defaultProperties()));
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> registerDefaultProperties(String name, Supplier<B> blockSupplier, BiFunction<B, Item.Properties, I> itemCreator, boolean doStateGen, boolean doModelGen, boolean doLangGen, LootTableDrop lootTable) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, ONIUtils.defaultProperties()), doStateGen, doModelGen, doLangGen, lootTable);
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> registerDefaultProperties(String name, Supplier<B> blockSupplier, BiFunction<B, Item.Properties, I> itemCreator, ONIBlockRegistryData data) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, ONIUtils.defaultProperties()), data);
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> register(String name, Supplier<B> blockSupplier, Function<B, I> itemCreator) {
        return register(name, blockSupplier, itemCreator, false, true, true, LootTableDrop.dropSelf());
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> register(String name, Supplier<B> blockSupplier, Function<B, I> itemCreator, boolean doStateGen, boolean doModelGen, boolean doLangGen, LootTableDrop lootTable) {
        return register(name, blockSupplier, itemCreator, new ONIBlockRegistryData(doStateGen, doModelGen, doLangGen, lootTable));
    }

    public <B extends Block, I extends BlockItem> DeferredBlock<B, I> register(String name, Supplier<B> blockSupplier, Function<B, I> itemCreator, ONIBlockRegistryData registryData) {
        DeferredHolder<Block, B> blockHolder = blocks.register(name, blockSupplier);
        DeferredHolder<Item, I> itemHolder = blockItems.register(name, () -> itemCreator.apply(blockHolder.get()));
        DeferredBlock<B, I> registeredBlock = new DeferredBlock<>(blockHolder, itemHolder);
        allBlocks.add(new Tuple<>(registeredBlock, registryData));
        return registeredBlock;
    }

    public record DeferredBlock<B extends Block, I extends BlockItem>(DeferredHolder<Block, B> block, DeferredHolder<Item, I> blockItem) {}

    public List<Tuple<DeferredBlock<?, ?>, ONIBlockRegistryData>> getAllBlocks() {
        return allBlocks;
    }

    public void register(IEventBus eventBus) {
        blocks.register(eventBus);
        blockItems.register(eventBus);
    }
}
