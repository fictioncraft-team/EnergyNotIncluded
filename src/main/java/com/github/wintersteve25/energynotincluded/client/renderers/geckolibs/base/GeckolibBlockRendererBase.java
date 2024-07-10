package com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GeckolibBlockRendererBase<T extends BlockEntity & GeoBlockEntity> extends GeoBlockRenderer<T> {
    public GeckolibBlockRendererBase(GeoModel<T> modelProvider) {
        super(modelProvider);
    }

    @Override
    protected Direction getFacing(T block) {
        return super.getFacing(block).getOpposite();
    }
}
