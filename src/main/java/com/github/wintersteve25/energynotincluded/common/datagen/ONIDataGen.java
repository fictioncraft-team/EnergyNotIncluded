package com.github.wintersteve25.energynotincluded.common.datagen;

import net.minecraft.data.DataGenerator;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIEngLangProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIModelProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIStateProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.server.ONILootTableProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ONIUtils.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ONIDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeServer(), ONILootTableProvider.create(gen.getPackOutput(), event.getLookupProvider()));

        gen.addProvider(event.includeClient(), new ONIStateProvider(gen.getPackOutput(), existingFileHelper));
        gen.addProvider(event.includeClient(), new ONIModelProvider(gen.getPackOutput(), existingFileHelper));

        //en_US
        gen.addProvider(event.includeClient(), new ONIEngLangProvider(gen.getPackOutput()));
    }
}
