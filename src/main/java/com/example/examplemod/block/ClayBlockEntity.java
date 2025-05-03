package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

public class ClayBlockEntity extends BlockEntity {
    /**
     * How many layers have been formed so far.
     */
    private int height = 1;

    /**
     * Which voxels have been formed in the current layer.
     */
    private BitSet formed = new BitSet(16 * 16);

    /**
     * The current forming recipe.
     */
    private String recipe = "";

    public ClayBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleMod.CLAY_FORMING_BE.get(), pos, state);
        formed.set(0, 16 * 16); // first layer is fully formed
    }

    public int getHeight() {
        return height;
    }

    public boolean getVoxel(int x, int z) {
        return formed.get(getBitIndex(x, z));
    }

    public void addVoxel(int x, int z) {
        formed.set(getBitIndex(x, z));
    }

    public void removeVoxel(int x, int z) {
        formed.clear(getBitIndex(x, z));
    }

    public void toggleVoxel(int x, int z) {
        formed.flip(getBitIndex(x, z));
    }

    private static int getBitIndex(int x, int z) {
        return x + z * 16;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("recipe", recipe);
        tag.putByte("height", (byte) height);
        tag.putLongArray("formed", formed.toLongArray());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        recipe = tag.getString("recipe");
        height = tag.getByte("height");
        formed = BitSet.valueOf(tag.getLongArray("formed"));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
