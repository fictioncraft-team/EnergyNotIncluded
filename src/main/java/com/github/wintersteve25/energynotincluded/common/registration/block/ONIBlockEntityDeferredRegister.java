package com.github.wintersteve25.energynotincluded.common.registration.block;

import com.github.wintersteve25.energynotincluded.common.data.capability.BECapability;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.util.BiConsumer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ONIBlockEntityDeferredRegister {
    private final DeferredRegister<BlockEntityType<?>> register;
    private final List<BECapability<?>> caps = new ArrayList<>();

    public ONIBlockEntityDeferredRegister(String modid) {
        register = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modid);
    }

    public <T extends Block, B extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> register(ONIBlockDeferredRegister.DeferredBlock<T, ?> block, BlockEntityType.BlockEntitySupplier<B> constructor) {
        return register(block, constructor, null);
    }

    public <T extends Block, B extends BlockEntity, CT, CC> DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> register(
            ONIBlockDeferredRegister.DeferredBlock<T, ?> block,
            BlockEntityType.BlockEntitySupplier<B> constructor,
            @Nullable BiConsumer<RegisterCapabilitiesEvent, BlockEntityType<B>> registerCapabilities
    ) {
        var type = register.register(block.block().getId().getPath(), () -> BlockEntityType.Builder.of(constructor, block.block().get()).build(null));
        if (registerCapabilities != null)
            caps.add(new BECapability<B>(type, registerCapabilities));
        return type;
    }

    public void register(IEventBus bus) {
        register.register(bus);
    }

    @SuppressWarnings("unchecked")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BECapability cap : caps) {
            cap.registrationFunc().accept(event, cap.ibeType().get());
        }
    }
}
