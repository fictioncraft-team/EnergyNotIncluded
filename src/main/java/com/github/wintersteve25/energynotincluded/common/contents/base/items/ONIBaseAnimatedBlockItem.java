package com.github.wintersteve25.energynotincluded.common.contents.base.items;

import com.github.wintersteve25.energynotincluded.client.utils.ItemRendererWrapper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseBlock;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ONIBaseAnimatedBlockItem extends ONIBaseItemBlock implements GeoItem {

    private final Supplier<ItemRendererWrapper> animatedModel;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public ONIBaseAnimatedBlockItem(ONIBaseBlock blockIn, Properties builder, Supplier<ItemRendererWrapper> animatedModel) {
        super(blockIn, builder);
        this.animatedModel = animatedModel;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BlockEntityWithoutLevelRenderer ister;
            
            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (ister == null) {
                    ister = animatedModel.get().renderer().get();
                }
                
                return ister;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
