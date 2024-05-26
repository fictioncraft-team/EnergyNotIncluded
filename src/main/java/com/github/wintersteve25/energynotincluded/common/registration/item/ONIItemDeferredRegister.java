package com.github.wintersteve25.energynotincluded.common.registration.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ONIItemDeferredRegister {
    private final Map<DeferredHolder<Item, ? extends Item>, ONIItemRegistryData> allItems = new HashMap<>();
    private final DeferredRegister<Item> register;

    public ONIItemDeferredRegister(String modid) {
        register = DeferredRegister.create(Registries.ITEM, modid);
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Function<Item.Properties, ITEM> sup) {
        return register(name, () -> sup.apply(ONIUtils.defaultProperties()));
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Function<Item.Properties, ITEM> sup, boolean doModelGen, boolean doLangGen) {
        return register(name, () -> sup.apply(ONIUtils.defaultProperties()), doModelGen, doLangGen);
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Function<Item.Properties, ITEM> sup, ONIItemRegistryData registryData) {
        return register(name, () -> sup.apply(ONIUtils.defaultProperties()), registryData);
    }

    public <ENTITY extends Mob> DeferredHolder<Item, DeferredSpawnEggItem> registerSpawnEgg(DeferredHolder<EntityType<?>, EntityType<ENTITY>> entityTypeProvider, int primaryColor, int secondaryColor) {
        return register(entityTypeProvider.getRegisteredName() + "_spawn_egg", props -> new DeferredSpawnEggItem(entityTypeProvider, primaryColor, secondaryColor, props));
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Supplier<? extends ITEM> sup) {
        DeferredHolder<Item, ITEM> registeredItem = register.register(name, sup);
        allItems.put(registeredItem, new ONIItemRegistryData(true, true));
        return registeredItem;
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Supplier<? extends ITEM> sup, boolean doModelGen, boolean doLangGen) {
        DeferredHolder<Item, ITEM> registeredItem = register.register(name, sup);
        allItems.put(registeredItem, new ONIItemRegistryData(doModelGen, doLangGen));
        return registeredItem;
    }

    public <ITEM extends Item> DeferredHolder<Item, ITEM> register(String name, Supplier<? extends ITEM> sup, ONIItemRegistryData data) {
        DeferredHolder<Item, ITEM> registeredItem = register.register(name, sup);
        allItems.put(registeredItem, data);
        return registeredItem;
    }

    public void register(IEventBus eventBus) {
        register.register(eventBus);
    }

    public Map<DeferredHolder<Item, ? extends Item>, ONIItemRegistryData> getAllItems() {
        return allItems;
    }
}
