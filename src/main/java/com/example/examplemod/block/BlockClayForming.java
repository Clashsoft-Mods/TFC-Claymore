package com.example.examplemod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockClayForming extends Block implements EntityBlock {
    public BlockClayForming(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ClayBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ClayBlockEntity clayBlockEntity) {
            final int height = clayBlockEntity.getHeight();
            if (height > 0) {
                return Shapes.box(0, 0, 0, 1, height / 16.0, 1);
            }
        }
        return Shapes.empty();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        final ItemStack handItem = player.getItemInHand(hand);
        // TODO support other types of clay
        if (handItem.getItem() == Items.CLAY_BALL) {
            final BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ClayBlockEntity clayBlockEntity) {
                final int x = (int) ((hit.getLocation().x - pos.getX()) * 16);
                final int z = (int) ((hit.getLocation().z - pos.getZ()) * 16);
                clayBlockEntity.toggleVoxel(x, z);
                clayBlockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
