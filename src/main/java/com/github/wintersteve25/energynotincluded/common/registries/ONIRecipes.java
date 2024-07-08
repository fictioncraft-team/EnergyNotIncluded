package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipeSerializer;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipeType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ONIRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, ONIUtils.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ONIUtils.MODID);

    public static final DeferredHolder<RecipeType<?>, BlueprintRecipeType> BLUEPRINT_RECIPE_TYPE = RECIPE_TYPE_DEFERRED_REGISTER.register("blueprint", BlueprintRecipeType::new);
    public static final DeferredHolder<RecipeSerializer<?>, BlueprintRecipeSerializer> BLUEPRINT_RECIPE_SERIALIZER = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("blueprint", () -> BlueprintRecipeSerializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPE_DEFERRED_REGISTER.register(eventBus);
        RECIPE_SERIALIZER_DEFERRED_REGISTER.register(eventBus);
    }
}
