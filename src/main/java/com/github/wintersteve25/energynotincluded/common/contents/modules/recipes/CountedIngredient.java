package com.github.wintersteve25.energynotincluded.common.contents.modules.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;
import java.util.function.Predicate;

public record CountedIngredient(Ingredient ingredient, int count) implements Predicate<ItemStack> {
    
    public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY
                    .fieldOf("item")
                    .forGetter(CountedIngredient::ingredient),
            Codec.INT
                    .fieldOf("count")
                    .forGetter(CountedIngredient::count)
    ).apply(instance, CountedIngredient::new));
    
    @Override
    public boolean test(ItemStack itemStack) {
        return ingredient.test(itemStack);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CountedIngredient) obj;
        return Objects.equals(this.ingredient, that.ingredient) &&
                this.count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, count);
    }

    @Override
    public String toString() {
        return "PartialItemIngredient[" +
                "ingredient=" + ingredient + ", " +
                "count=" + count + ']';
    }
}
