package com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints;

import com.github.wintersteve25.energynotincluded.common.registries.ONIRecipes;
import com.github.wintersteve25.energynotincluded.common.utils.PartialItemIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record BlueprintRecipe(ResourceLocation id, NonNullList<PartialItemIngredient> ingredients, BlockItem output) implements Recipe<Container> {

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

    public static Optional<BlueprintRecipe> getRecipeWithId(Level level, ResourceLocation id) {
        return level.getRecipeManager()
                .getAllRecipesFor(ONIRecipes.BLUEPRINT_RECIPE_TYPE.get())
                .stream()
                .filter(r -> r.id().equals(id))
                .findFirst();
    }
}
