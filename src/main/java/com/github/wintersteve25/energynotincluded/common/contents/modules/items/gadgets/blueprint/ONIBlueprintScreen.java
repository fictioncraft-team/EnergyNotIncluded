package com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint;

import com.github.wintersteve25.energynotincluded.client.utils.IngredientTooltip;
import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIIItem;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.registries.ONIDataComponents;
import com.github.wintersteve25.energynotincluded.common.registries.ONIRecipes;
import com.github.wintersteve25.tau.components.base.DynamicUIComponent;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.interactable.Button;
import com.github.wintersteve25.tau.components.interactable.ListView;
import com.github.wintersteve25.tau.components.layout.*;
import com.github.wintersteve25.tau.components.render.Render;
import com.github.wintersteve25.tau.components.utils.*;
import com.github.wintersteve25.tau.components.utils.Container;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.*;
import com.github.wintersteve25.tau.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ONIBlueprintScreen extends DynamicUIComponent {

    private static final Lazy<List<RecipeHolder<BlueprintRecipe>>> RECIPES =
            Lazy.of(() -> Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ONIRecipes.BLUEPRINT_RECIPE_TYPE.get()));

    private Set<RecipeHolder<BlueprintRecipe>> recipesShown;

    private UIComponent categoryButtons() {
        return new Row.Builder()
                .withSpacing(2)
                .build(
                        new Category("Base", RECIPES.get()
                                .stream()
                                .filter(recipe -> recipe.value().output() instanceof ONIIItem i && i.getONIItemCategory() == ONIItemCategory.GENERAL)
                                .collect(Collectors.toSet()), recipes -> {
                            recipesShown = recipes;
                            rebuild();
                        }),
                        new Category("Power", RECIPES.get()
                                .stream()
                                .filter(recipe -> recipe.value().output() instanceof ONIIItem i && i.getONIItemCategory() == ONIItemCategory.POWER)
                                .collect(Collectors.toSet()), recipes -> {
                            recipesShown = recipes;
                            rebuild();
                        }));
    }

    @Override
    public UIComponent build(Layout layout, Theme theme) {
        if (recipesShown == null) {
            return new Align.Builder()
                    .withVertical(LayoutSetting.END)
                    .build(new Padding(
                            new Pad.Builder().left(2).bottom(2).build(),
                            new Column.Builder()
                                    .withSpacing(2)
                                    .withAlignment(LayoutSetting.START)
                                    .build(
                                            new Sized(
                                                    Size.staticSize(150, 200),
                                                    new Container.Builder()
                                                            .noBackground()),
                                            categoryButtons()
                                    )));
        }

        return new Align.Builder()
                .withVertical(LayoutSetting.END)
                .build(new Padding(
                        new Pad.Builder().left(2).bottom(2).build(),
                        new Column.Builder()
                                .withSpacing(2)
                                .withAlignment(LayoutSetting.START)
                                .build(
                                        new Sized(
                                                Size.staticSize(192, 200),
                                                new Container.Builder()
                                                        .withChild(new ListView.Builder()
                                                                .withSpacing(2)
                                                                .withAlignment(LayoutSetting.START)
                                                                .build(recipesShown.stream().map(Building::new).collect(Collectors.toUnmodifiableList()))
                                                        )),
                                        categoryButtons()
                                )));
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ScreenUIRenderer(new ONIBlueprintScreen(), true));
    }

    private record Category(String categoryName, Set<RecipeHolder<BlueprintRecipe>> recipes,
                            Consumer<Set<RecipeHolder<BlueprintRecipe>>> onActivate) implements UIComponent {
        @Override
        public UIComponent build(Layout layout, Theme theme) {
            return new Sized(
                    Size.staticSize(80, 20),
                    new Button.Builder()
                            .withOnPress((btn) -> onActivate.accept(recipes))
                            .build(new Center(new Text.Builder(categoryName))));
        }
    }

    private record Building(RecipeHolder<BlueprintRecipe> recipe) implements UIComponent {
        @Override
        public UIComponent build(Layout layout, Theme theme) {
            return new Padding(
                    new Pad.Builder().top(4).left(4).right(4).build(),
                    new Sized(
                            Size.percentage(1, 0.1f),
                            new Tooltip.Builder()
                                    .withComponent(Component.literal("Requires:"))
                                    .with(recipe.value().ingredients()
                                            .stream()
                                            .map(ingredient -> (ClientTooltipComponent) new IngredientTooltip(ingredient))
                                            .toList())
                                    .build(new Button.Builder()
                                            .withOnPress(btn -> {
                                                if (btn != 0) return;
                                                Minecraft.getInstance().setScreen(null);
                                                ItemStack heldItem = Minecraft.getInstance().player.getUseItem();
                                                if (!(heldItem.getItem() instanceof ONIBlueprintItem)) {
                                                    return;
                                                }
                                                
                                                heldItem.set(ONIDataComponents.BLUEPRINT_ITEM_DATA, new ONIBlueprintItem.ItemData(recipe().id()));
                                            })
                                            .build(new Row.Builder()
                                                    .withSizeBehaviour(FlexSizeBehaviour.MAX)
                                                    .build(
                                                            new Spacer(new SimpleVec2i(2, 0)),
                                                            new Sized(
                                                                    Size.staticSize(17, 17),
                                                                    new Render(new ItemRenderProvider(recipe.value().output()))),
                                                            new Text.Builder(recipe.value().output().getName(new ItemStack(recipe.value().output())))
                                                                    .withColor(Color.WHITE))))
                    )
            );
        }
    }
}
