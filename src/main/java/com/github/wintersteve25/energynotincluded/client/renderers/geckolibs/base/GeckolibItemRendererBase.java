package com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base;

import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GeckolibItemRendererBase<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    public GeckolibItemRendererBase(GeoModel<T> modelProvider) {
        super(modelProvider);
    }
}
