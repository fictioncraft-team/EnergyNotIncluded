package com.github.wintersteve25.energynotincluded.common.events;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder.ONIPlaceHolderBER;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.client.keybinds.ONIKeybinds;
import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibBlockRendererBase;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.GermEventsHandler;
import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenGui;
import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = ONIUtils.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModClientEventsHandler {

    @SubscribeEvent
    public static void clientPreInit(final FMLClientSetupEvent event) {
        //Keybindings
        ONIKeybinds.init();

        //Events
        NeoForge.EVENT_BUS.addListener(GermEventsHandler::itemToolTipEvent);

        //GUI Attachments
        MenuScreens.register(ONIBlocks.Machines.Power.COAL_GEN_CONTAINER.get(), CoalGenGui::new);

        //Entities
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (ModList.get().isLoaded("geckolib3")) {
            event.registerBlockEntityRenderer(ONIBlocks.Machines.Power.COAL_GEN_TE.get(), t -> new GeckolibBlockRendererBase<CoalGenTE>(t, ONIConstants.Geo.COAL_GEN_TE));
        }

        event.registerBlockEntityRenderer(ONIBlocks.Misc.PLACEHOLDER_TE.get(), t -> new ONIPlaceHolderBER(t.getBlockRenderDispatcher()));
    }

    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS) {
            event.addSprite(ONIConstants.Resources.CURIOS_GOGGLES);
        }
    }
}
