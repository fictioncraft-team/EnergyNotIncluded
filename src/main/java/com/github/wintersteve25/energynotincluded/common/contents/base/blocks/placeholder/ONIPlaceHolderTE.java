package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseInvTE;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.CountedIngredient;
import com.github.wintersteve25.energynotincluded.common.utils.SerializableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Optional;

public class ONIPlaceHolderTE extends ONIBaseInvTE {

    private RecipeHolder<BlueprintRecipe> recipe;
    private SerializableMap<CountedIngredient, Integer> remainingIngredients;
    private float completionPercentage;

    public ONIPlaceHolderTE(BlockPos pos, BlockState state) {
        this(ONIBlocks.PLACEHOLDER_TE.get(), pos, state);
    }

    public ONIPlaceHolderTE(BlockEntityType<ONIPlaceHolderTE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getInvSize() {
        return 32;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        completionPercentage = tag.getFloat("completion");
        if (!tag.contains("recipe")) return;
        init(BlueprintRecipe.getRecipeWithId(level, new ResourceLocation(tag.getString("recipe"))).orElseThrow());
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putFloat("completion", completionPercentage);
        if (recipe == null) return;
        tag.putString("recipe", recipe.id().toString());
    }

    public void init(RecipeHolder<BlueprintRecipe> recipe) {
        if (hasItem()) {
            throw new IllegalStateException("Should not initialize Placeholder with recipe after it is initialized and has items");
        }

        this.recipe = recipe;
        this.remainingIngredients = new SerializableMap<>(
                ing -> NbtOps.INSTANCE.withEncoder(CountedIngredient.CODEC)
                        .apply(ing)
                        .getOrThrow(),
                IntTag::valueOf,
                tag -> NbtOps.INSTANCE.withDecoder(CountedIngredient.CODEC)
                        .apply(tag)
                        .getOrThrow()
                        .getFirst(),
                tag -> ((IntTag) tag).getAsInt(),
                Tag.TAG_COMPOUND,
                Tag.TAG_INT
        );
        
        for (CountedIngredient ingredient : recipe.value().ingredients()) {
            this.remainingIngredients.put(ingredient, ingredient.count());
        }
        completionPercentage = remainingIngredients.isEmpty() ? 1 : 0f / remainingIngredients.size();
    }

    public boolean addItem(ItemStack itemStack) {
        if (recipe == null) return false;
        Optional<Map.Entry<CountedIngredient, Integer>> ingredient = remainingIngredients.entrySet()
                .stream()
                .filter(ing -> ing.getKey().test(itemStack))
                .findFirst();

        if (ingredient.isEmpty()) return false;
        for (int i = 0; i < getInvSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                 continue;
            }

            itemHandler.insertItem(i, itemStack.copy(), false);

            int currCount = ingredient.get().getValue();
            int shrinkCount = Math.min(itemStack.getCount(), currCount);
            itemStack.shrink(shrinkCount);
            
            this.remainingIngredients.put(ingredient.get().getKey(), currCount - shrinkCount);
            if (currCount - shrinkCount <= 0) {
                remainingIngredients.remove(ingredient.get().getKey());
            }

            completionPercentage = 0f / remainingIngredients.size();
            updateBlock();
            return true;
        }

        return false;
    }

    public BlockItem getInPlaceOf() {
        if (recipe == null) {
            return null;
        }

        return recipe.value().output();
    }

    public float getCompletionPercentage() {
        return completionPercentage;
    }
}
