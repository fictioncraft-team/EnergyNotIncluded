package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.placeholder;

import com.github.wintersteve25.energynotincluded.client.utils.MultiplyAlphaRenderTypeBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Math;
import org.joml.Vector3f;

public class ONIPlaceHolderBER implements BlockEntityRenderer<ONIPlaceHolderTE> {

    private static final Vector3f YP = new Vector3f(0, 1, 0);
    
    private final BlockRenderDispatcher renderDispatcher;

    public ONIPlaceHolderBER(BlockRenderDispatcher renderDispatcher) {
        this.renderDispatcher = renderDispatcher;
    }

    @Override
    public void render(ONIPlaceHolderTE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockState blockItem = pBlockEntity.getInPlaceOf();
        if (blockItem == null) return;

        pPoseStack.pushPose();

        pPoseStack.translate(0.5f, -0.5f, 0.5f);
        Direction direction = pBlockEntity.getBlockState().getValue(DirectionalBlock.FACING);
        rotateBasedOnDirection(pPoseStack, direction);
        pPoseStack.translate(-0.5f, 0.5f, -0.5f);
        pPoseStack.translate(0, -0.5f, 0);

        MultiplyAlphaRenderTypeBuffer buffer = new MultiplyAlphaRenderTypeBuffer(Minecraft.getInstance().renderBuffers().bufferSource(), Math.max(pBlockEntity.getCompletionPercentage(), 0.3f));
        renderDispatcher.renderSingleBlock(blockItem, pPoseStack, buffer, 15728640, OverlayTexture.RED_OVERLAY_V, ModelData.EMPTY, RenderType.translucent());

        pPoseStack.popPose();
    }

    private void rotateBasedOnDirection(PoseStack poseStack, Direction direction) {
        switch (direction) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotation(Math.toRadians(180)));
            case EAST -> poseStack.mulPose(Axis.YP.rotation(Math.toRadians(-90)));
            case WEST -> poseStack.mulPose(Axis.YP.rotation(Math.toRadians(90)));
            default -> {}
        }
    }
}
