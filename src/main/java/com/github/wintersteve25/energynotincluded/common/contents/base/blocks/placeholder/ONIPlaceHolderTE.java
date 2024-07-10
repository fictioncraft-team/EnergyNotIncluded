package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseBoundingMachine;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseInvTE;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.CountedIngredient;
import com.github.wintersteve25.energynotincluded.common.utils.SerializableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.Map;
import java.util.Optional;

public class ONIPlaceHolderTE extends ONIBaseInvTE {

    private ResourceLocation recipeId;
    private Lazy<BlueprintRecipe> recipe;
    private SerializableMap<CountedIngredient, Integer> remainingIngredients;
    private float completionPercentage;
    
    private Vec3 placeClickedLoc;
    private Direction placeClickedFace;

    public ONIPlaceHolderTE(BlockPos pos, BlockState state) {
        this(ONIBlocks.PLACEHOLDER_TE.get(), pos, state);
    }

    public ONIPlaceHolderTE(BlockEntityType<ONIPlaceHolderTE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readSavedAndSyncedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.readSavedAndSyncedData(tag, provider);
        completionPercentage = tag.getFloat("completion");
        recipeId = new ResourceLocation(tag.getString("recipe"));
        recipe = Lazy.of(() -> BlueprintRecipe.getRecipeWithId(level, recipeId).get().value());
    }

    @Override
    public void writeSavedAndSyncedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.writeSavedAndSyncedData(tag, provider);
        tag.putFloat("completion", completionPercentage);
        tag.putString("recipe", recipeId.toString());
    }

    @Override
    public void readSavedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.readSavedData(tag, provider);
        if (!tag.contains("placeClickedLoc")) return;
        CompoundTag placeClickedLocTag = tag.getCompound("placeClickedLoc");
        placeClickedLoc = new Vec3(placeClickedLocTag.getDouble("x"), placeClickedLocTag.getDouble("y"), placeClickedLocTag.getDouble("z"));
        placeClickedFace = Direction.byName(tag.getString("placeClickedFace"));
        remainingIngredients = createMap();
        remainingIngredients.deserializeNBT(provider, tag.getCompound("remainingIngredients"));
    }

    @Override
    public void writeSavedData(CompoundTag tag, HolderLookup.Provider provider) {
        super.writeSavedData(tag, provider);
        CompoundTag placeCLickedLocTag = new CompoundTag();
        if (placeClickedLoc == null) return;
        placeCLickedLocTag.putDouble("x", placeClickedLoc.x);
        placeCLickedLocTag.putDouble("y", placeClickedLoc.y);
        placeCLickedLocTag.putDouble("z", placeClickedLoc.z);
        tag.put("placeClickedLoc", placeCLickedLocTag);
        tag.putString("placeClickedFace", placeClickedFace.getSerializedName());
        tag.put("remainingIngredients", remainingIngredients.serializeNBT(provider));
    }

    public void init(RecipeHolder<BlueprintRecipe> recipe, Vec3 placeClickedLoc, Direction face) {
        this.recipe = Lazy.of(recipe::value);
        this.recipeId = recipe.id();
        this.remainingIngredients = createMap();
        
        this.placeClickedLoc = placeClickedLoc;
        this.placeClickedFace = face;

        for (CountedIngredient ingredient : recipe.value().ingredients()) {
            this.remainingIngredients.put(ingredient, ingredient.count());
        }

        completionPercentage = remainingIngredients.isEmpty() ? 1 : 1 - ((float) remainingIngredients.size() / recipe.value().ingredients().size());
        
        if (recipe.value().output().getBlock() instanceof ONIBaseBoundingMachine<?> boundingMachine) {
            ONIBaseBoundingMachine.buildBoundingMachine(level, getBlockState().getValue(DirectionalBlock.FACING), getBlockPos(), boundingMachine);
        }
    }

    public boolean addItem(Player player, InteractionHand hand, ItemStack itemStack) {
        if (recipe == null) return false;
        ItemStack finalItemStack = itemStack;
        Optional<Map.Entry<CountedIngredient, Integer>> ingredient = remainingIngredients.entrySet()
                .stream()
                .filter(ing -> ing.getKey().test(finalItemStack))
                .findFirst();

        if (ingredient.isEmpty()) return false;
        CountedIngredient itemShouldBe = ingredient.get().getKey();
        int itemCountLeft = ingredient.get().getValue();
        int shrinkCount = Math.min(itemCountLeft, itemStack.getCount());
        ItemStack copy = itemStack.copy();

        itemStack.shrink(shrinkCount);

        if (itemCountLeft - shrinkCount <= 0) {
            remainingIngredients.remove(itemShouldBe);
        } else {
            this.remainingIngredients.put(itemShouldBe, itemCountLeft - shrinkCount);
        }

        completionPercentage = 1 - ((float) remainingIngredients.size() / recipe.get().ingredients().size());

        int i = 0;
        itemStack = new ItemStack(copy.getItem(), shrinkCount);

        while (i < itemHandler.getSlots()) {
            if (itemHandler.insertItem(i, itemStack, true) == itemStack) {
                i++;
                continue;
            }

            itemStack = itemHandler.insertItem(i, itemStack.copy(), false);
            if (itemStack.isEmpty()) {
                break;
            }

            i++;
        }

        if (completionPercentage >= 1 && remainingIngredients.isEmpty()) {
            build(player, hand);
            return true;
        }

        updateBlock();
        return true;
    }

    public BlockState getInPlaceOf() {
        if (level == null) {
            return null;
        }

        if (recipe == null) {
            return null;
        }

        return recipe.get().output();
    }

    public float getCompletionPercentage() {
        return completionPercentage;
    }

    @Override
    public int getInvSize() {
        return 32;
    }

    private SerializableMap<CountedIngredient, Integer> createMap() {
        return new SerializableMap<>(
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
    }

    private void build(Player player, InteractionHand hand) {
        if (level == null) return;
        if (level.isClientSide()) return;
        if (recipe == null) return;

        Block otherBlock = recipe.get().output().getBlock();
        Item other = otherBlock.asItem();
        if (!(other instanceof BlockItem blockItem)) return;
        level.removeBlock(getBlockPos(), false);
        blockItem.place(new BlockPlaceContext(level, player, hand, ItemStack.EMPTY, new BlockHitResult(placeClickedLoc, placeClickedFace, getBlockPos(), false)));
    }
}
