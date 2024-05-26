package com.github.wintersteve25.energynotincluded.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIEngLangProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIModelProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.client.ONIStateProvider;
import com.github.wintersteve25.energynotincluded.common.datagen.server.ONILootTableProvider;

@Mod.EventBusSubscriber(modid = ONIUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ONIDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            gen.addProvider(new ONILootTableProvider(gen));
        }

        if (event.includeClient()) {
            gen.addProvider(new ONIStateProvider(gen, existingFileHelper));
            gen.addProvider(new ONIModelProvider(gen, existingFileHelper));

            //en_US
            gen.addProvider(new ONIEngLangProvider(gen));
        }
    }
}
