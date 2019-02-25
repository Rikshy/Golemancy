package de.shyrik.golemancy.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAIHarvest extends EntityAIMoveToBlock {

    protected EntityCreature creature;
    public EntityAIHarvest(EntityCreature creature, double speedIn, int length) {
        super(creature, speedIn, length);
        this.creature = creature;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        creature.getLookHelper().setLookPosition(
                destinationBlock.getX() + 0.5D,
                destinationBlock.getY() + 1.0D,
                destinationBlock.getZ() + 0.5D,
                10.0F,
                (float) creature.getVerticalFaceSpeed());

        if (getIsAboveDestination()) {
            World worldIn = creature.world;
            BlockPos pos = destinationBlock.up();
            if (shouldMoveTo(worldIn, destinationBlock) && worldIn.destroyBlock(pos, true))
                worldIn.setBlockState(pos, worldIn.getBlockState(pos).getBlock().getDefaultState());
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull IWorldReaderBase worldIn, @Nonnull BlockPos pos) {
        IBlockState block = worldIn.getBlockState(pos.up());
        if (block.getBlock() instanceof BlockCrops)
            return ((BlockCrops) block.getBlock()).isMaxAge(block);
        else return false;
    }
}
