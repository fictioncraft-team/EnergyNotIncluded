package com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public enum BlueprintRecipeSerializer implements RecipeSerializer<BlueprintRecipe> {
    INSTANCE;

    @Override
    public MapCodec<BlueprintRecipe> codec() {
        return BlueprintRecipe.MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlueprintRecipe> streamCodec() {
        return StreamCodec.of(this::toNetwork, this::fromNetwork);
    }

    private BlueprintRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        return buf.readJsonWithCodec(BlueprintRecipe.MAP_CODEC.codec());
    }

    private void toNetwork(RegistryFriendlyByteBuf buf, BlueprintRecipe recipe) {
        buf.writeJsonWithCodec(BlueprintRecipe.MAP_CODEC.codec(), recipe);
    }
}
