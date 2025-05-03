package com.example.examplemod.client;

import com.example.examplemod.block.ClayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockClayFormingRenderer implements BlockEntityRenderer {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public BlockClayFormingRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (entity instanceof ClayBlockEntity clayBlockEntity) {
            // For each voxel, render a clay block at the position of the voxel
            final BlockState clay = Blocks.CLAY.defaultBlockState();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (clayBlockEntity.getVoxel(x, z)) {
                        poseStack.pushPose();
                        poseStack.translate(x / 16.0, (clayBlockEntity.getHeight() - 1) / 16.0, z / 16.0);
                        poseStack.scale(1f / 16f, 1f / 16f, 1f / 16f);
                        blockRenderDispatcher.renderSingleBlock(clay, poseStack, buffer, combinedLight, combinedOverlay);
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}
