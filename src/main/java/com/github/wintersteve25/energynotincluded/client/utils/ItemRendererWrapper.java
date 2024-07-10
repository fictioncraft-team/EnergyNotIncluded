package com.github.wintersteve25.energynotincluded.client.utils;

import com.github.wintersteve25.energynotincluded.client.renderers.geckolibs.base.GeckolibItemRendererBase;

import java.util.function.Supplier;

public record ItemRendererWrapper(Supplier<? extends GeckolibItemRendererBase<?>> renderer) {
}
