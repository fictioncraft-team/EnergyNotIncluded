package com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints;

import com.github.wintersteve25.energynotincluded.common.registries.ONIRecipes;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.CountedIngredient;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record BlueprintRecipe(List<CountedIngredient> ingredients, BlockItem output) implements Recipe<Container> {

    public static final MapCodec<BlueprintRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CountedIngredient.CODEC
                    .listOf()
                    .fieldOf("inputs")
                    .forGetter(BlueprintRecipe::ingredients),
            ItemStack.STRICT_CODEC
                    .validate(itemStack -> {
                        if (itemStack.getItem() instanceof BlockItem) {
                            return DataResult.success(itemStack);
                        }
                        
                        return DataResult.error(() -> "");
                    })
                    .xmap(itemStack -> (BlockItem) itemStack.getItem(), ItemStack::new)
                    .fieldOf("output")
                    .forGetter(BlueprintRecipe::output)
    ).apply(instance, BlueprintRecipe::new));

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer, HolderLookup.Provider registeries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ONIRecipes.BLUEPRINT_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ONIRecipes.BLUEPRINT_RECIPE_TYPE.get();
    }

    public static Optional<RecipeHolder<BlueprintRecipe>> getRecipeWithId(Level level, ResourceLocation id) {
        return level.getRecipeManager()
                .getAllRecipesFor(ONIRecipes.BLUEPRINT_RECIPE_TYPE.get())
                .stream()
                .filter(r -> r.id().equals(id))
                .findFirst();
    }
}
