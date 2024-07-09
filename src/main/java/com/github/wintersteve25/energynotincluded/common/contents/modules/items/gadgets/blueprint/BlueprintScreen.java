package com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint;

import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.registries.ONIRecipes;
import com.github.wintersteve25.tau.components.base.DynamicUIComponent;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.utils.Container;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.theme.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;

public class BlueprintScreen extends DynamicUIComponent {

    private static final Lazy<List<RecipeHolder<BlueprintRecipe>>> RECIPES =
            Lazy.of(() -> Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ONIRecipes.BLUEPRINT_RECIPE_TYPE.get()));

    @Override
    public UIComponent build(Layout layout, Theme theme) {
        return new Container.Builder();
    }
}
