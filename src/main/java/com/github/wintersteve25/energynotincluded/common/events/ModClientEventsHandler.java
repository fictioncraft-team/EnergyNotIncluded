package com.github.wintersteve25.energynotincluded.common.events;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder.ONIPlaceHolderBER;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibBlockRendererBase;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ONIUtils.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModClientEventsHandler {

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (ModList.get().isLoaded("geckolib")) {
//            event.registerBlockEntityRenderer(ONIBlocks.Machines.Power.COAL_GEN_TE.get(), t -> new GeckolibBlockRendererBase<>(ONIConstants.Geo.COAL_GEN_TE));
        }

//        event.registerBlockEntityRenderer(ONIBlocks.Misc.PLACEHOLDER_TE.get(), t -> new ONIPlaceHolderBER(t.getBlockRenderDispatcher()));
    }
}
