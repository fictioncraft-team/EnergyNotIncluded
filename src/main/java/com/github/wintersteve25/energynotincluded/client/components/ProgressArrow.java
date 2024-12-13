package com.github.wintersteve25.energynotincluded.client.utils;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.tau.components.animated.AnimatedTexture;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.layout.Stack;
import com.github.wintersteve25.tau.components.utils.Texture;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.FlexSizeBehaviour;
import com.github.wintersteve25.tau.utils.SimpleVec2i;
import com.github.wintersteve25.tau.utils.Variable;
import net.minecraft.resources.ResourceLocation;

public class ProgressArrow implements UIComponent {

    private Variable<Float> progress;
    private Variable<SimpleVec2i> uvSize;

    public ProgressArrow(Variable<Float> progress) {
        this.progress = progress;
        uvSize = new Variable<>(new SimpleVec2i(0, 17));
        this.progress.addListener(f -> {
            int w = (int) (23 * f);
            uvSize.setValue(new SimpleVec2i(w, 16));
        });
    }

    @Override
    public UIComponent build(Layout layout, Theme theme) {
        ResourceLocation tex = new ResourceLocation(ONIUtils.MODID, "textures/gui/widgets.png");

        return new Stack(
                FlexSizeBehaviour.MIN,
                new Texture.Builder(tex)
                        .withSize(new SimpleVec2i(23, 16))
                        .withUv(new SimpleVec2i(18, 85))
                        .withUvSize(new SimpleVec2i(24, 16)),
                new AnimatedTexture(
                        tex,
                        new SimpleVec2i(256, 256),
                        new SimpleVec2i(23, 16),
                        uvSize,
                        new Variable<>(new SimpleVec2i(39, 16)),
                        false
                )
        );
    }
}
