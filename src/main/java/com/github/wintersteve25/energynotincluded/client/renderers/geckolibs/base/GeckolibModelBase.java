package com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.ResoureceLocationHelper;

public class GeckolibModelBase<T extends GeoAnimatable> extends GeoModel<T> {
    private final ResourceLocation rl1;
    private final ResourceLocation rl2;
    private final ResourceLocation rl3;

    public GeckolibModelBase(String pathModel, String pathTexture, String pathAnimation) {
        this.rl1 = ResoureceLocationHelper.ResourceLocationBuilder.getBuilder()
                .geoModel()
                .addPath(pathModel)
                .build();
        this.rl2 = new ResourceLocation(ONIUtils.MODID, "textures/block/" + pathTexture);
        this.rl3 = new ResourceLocation(ONIUtils.MODID, "animations/" + pathAnimation);
    }

    public GeckolibModelBase(GeckolibModelBase<?> other) {
        this.rl1 = other.rl1;
        this.rl2 = other.rl2;
        this.rl3 = other.rl3;
    }
    
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return rl1;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return rl2;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return rl3;
    }
}
