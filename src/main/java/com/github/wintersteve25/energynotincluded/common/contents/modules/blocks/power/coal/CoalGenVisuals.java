package com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal;

import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibItemRendererBase;
import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibModelBase;
import com.github.wintersteve25.energynotincluded.client.utils.ItemRendererWrapper;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseAnimatedBlockItem;

import java.util.function.Supplier;

public class CoalGenVisuals {
    public static final GeckolibModelBase<CoalGenTE> COAL_GEN_TE = 
            new GeckolibModelBase<>(
                    "machines/power/coal_generator.geo.json",
                    "machines/power/coal_generator.png",
                    "machines/power/coal_generator.animation.json"
            );
    
    public static final GeckolibModelBase<ONIBaseAnimatedBlockItem> COAL_GEN_IB = new GeckolibModelBase<>(COAL_GEN_TE);
    public static final Supplier<ItemRendererWrapper> COAL_GEN_ISTER = () -> new ItemRendererWrapper(() -> new GeckolibItemRendererBase<>(COAL_GEN_IB));
}
