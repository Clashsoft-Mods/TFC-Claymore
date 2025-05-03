package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClayBlockEntity extends BlockEntity {
    public ClayBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleMod.CLAY_FORMING_BE.get(), pos, state);
    }
}
