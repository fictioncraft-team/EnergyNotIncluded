package com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import com.github.wintersteve25.energynotincluded.common.network.PacketUpdateBlueprintItem;
import com.github.wintersteve25.energynotincluded.common.registries.ONIDataComponents;
import com.github.wintersteve25.energynotincluded.common.registries.ONIRecipes;
import com.github.wintersteve25.tau.components.base.DynamicUIComponent;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.interactable.Button;
import com.github.wintersteve25.tau.components.interactable.ListView;
import com.github.wintersteve25.tau.components.layout.Align;
import com.github.wintersteve25.tau.components.layout.Row;
import com.github.wintersteve25.tau.components.render.Render;
import com.github.wintersteve25.tau.components.utils.Container;
import com.github.wintersteve25.tau.components.utils.Padding;
import com.github.wintersteve25.tau.components.utils.Sized;
import com.github.wintersteve25.tau.components.utils.Text;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;

public class BlueprintScreen extends DynamicUIComponent {

    private static final Lazy<List<RecipeHolder<BlueprintRecipe>>> RECIPES =
            Lazy.of(() -> Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ONIRecipes.BLUEPRINT_RECIPE_TYPE.get()));

    @Override
    public UIComponent build(Layout layout, Theme theme) {
        return new Align.Builder()
                .withVertical(LayoutSetting.END)
                .withHorizontal(LayoutSetting.START)
                .build(new Sized(Size.percentage(0.3f, 0.7f), new Container.Builder()
                        .withChild(new ListView.Builder()
                                .withSpacing(4)
                                .withAlignment(LayoutSetting.START)
                                .build(RECIPES.get().stream().map(this::recipe).toList()))));
    }

    private UIComponent recipe(RecipeHolder<BlueprintRecipe> recipe) {
        return new Sized((size) -> new SimpleVec2i(size.x, 40), new Container.Builder()
                .noBackground()
                .withChild(new Padding(new Pad(8, 8, 8, 8), new Align.Builder()
                        .withHorizontal(LayoutSetting.START)
                        .withVertical(LayoutSetting.CENTER)
                        .build(recipeContent(recipe)))));
    }
    
    private UIComponent recipeContent(RecipeHolder<BlueprintRecipe> recipe) {
        return new Button.Builder()
                .withOnPress(b -> ONINetworking.sendToServer(new PacketUpdateBlueprintItem(recipe.id())))
                .build(new Padding(new Pad(0, 0, 2, 2), itemPreview(recipe)));
    }
    
    private UIComponent itemPreview(RecipeHolder<BlueprintRecipe> recipe) {
        UIComponent icon = new Render(new ItemRenderProvider(recipe.value().output().getBlock()));
        
        return new Row.Builder()
                .withSpacing(0)
                .withSizeBehaviour(FlexSizeBehaviour.MIN)
                .build(
                        new Sized((size) -> new SimpleVec2i(16, size.y), new Container.Builder()),
                        new Text.Builder(recipe.value().output().getBlock().getName())
                );
    }
}
