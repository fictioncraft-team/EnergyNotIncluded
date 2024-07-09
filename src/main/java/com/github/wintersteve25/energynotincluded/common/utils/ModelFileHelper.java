package com.github.wintersteve25.energynotincluded.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class ModelFileHelper {
    public static ModelFile createModelFile(ResourceLocation location, boolean exists) {
        return new ModelFile(location) {
            @Override
            protected boolean exists() {
                return exists;
            }
        };
    }

    public static ModelFile createModelFile(ResourceLocation location) {
        return createModelFile(location, true);
    }

    public static ModelFile cubeAll(String modelName, ResourceLocation textureLoc, BlockModelProvider modelProvider) {
        return modelProvider.withExistingParent(modelName, "block/cube_all").texture("all", textureLoc);
    }
}
