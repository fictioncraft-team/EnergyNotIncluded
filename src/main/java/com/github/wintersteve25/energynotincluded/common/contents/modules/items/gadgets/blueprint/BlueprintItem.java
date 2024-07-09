package com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder.ONIPlaceHolderTE;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItem;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import com.github.wintersteve25.energynotincluded.common.network.PacketOpenUI;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIDataComponents;
import com.github.wintersteve25.energynotincluded.common.registries.ONIScreens;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BlueprintItem extends ONIBaseItem {
    
    public static final Codec<ItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC
                    .fieldOf("recipe")
                    .forGetter(ItemData::recipeSelected)
    ).apply(instance, ItemData::new));
    
    public static final StreamCodec<ByteBuf, ItemData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ItemData::recipeSelected,
            ItemData::new
    );

    public BlueprintItem() {
        super(ONIUtils.defaultProperties());
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Direction face = pContext.getClickedFace();
        BlockPos clickedPos = pContext.getClickedPos();
        BlockPos targetPosition = clickedPos.relative(face);
        Player player = pContext.getPlayer();

        Level level = pContext.getLevel();
        if (!level.isInWorldBounds(targetPosition)) return InteractionResult.PASS;

        BlockItem placeHolder = ONIBlocks.PLACEHOLDER_BLOCK.blockItem().get();
        InteractionResult result = placeHolder.place(new BlockPlaceContext(level, player, pContext.getHand(), ItemStack.EMPTY, new BlockHitResult(pContext.getClickLocation(), face, targetPosition, false)));

        if (!result.consumesAction()) return InteractionResult.PASS;
        if (level instanceof ServerLevel serverLevel) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(targetPosition);
            ItemData data = pContext.getItemInHand().get(ONIDataComponents.BLUEPRINT_ITEM_DATA.get());
            if (data == null) return InteractionResult.PASS;
            if (data.recipeSelected() == null) return InteractionResult.PASS;
            var recipe = BlueprintRecipe.getRecipeWithId(level, data.recipeSelected());
            if (recipe.isEmpty()) return InteractionResult.PASS;
            
            if (blockEntity instanceof ONIPlaceHolderTE placeHolderTE) {
                placeHolderTE.init(recipe.get());
                placeHolderTE.updateBlock();
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide()) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (pPlayer.isCrouching() && pPlayer.pick(5, 0, false).getType() == HitResult.Type.MISS) {
            ONINetworking.sendToClient(new PacketOpenUI(ONIScreens.BLUEPRINT), (ServerPlayer) pPlayer);
            return InteractionResultHolder.success(itemstack);
        }

        return InteractionResultHolder.pass(itemstack);
    }
    
    public static record ItemData(
            ResourceLocation recipeSelected
    ) {
    }
}
