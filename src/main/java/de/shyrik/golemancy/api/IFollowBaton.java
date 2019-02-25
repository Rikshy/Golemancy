package de.shyrik.golemancy.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IFollowBaton {

    BlockPos getPosition();

    @Nullable
    EntityPlayer getLeader();

    void setLeader(@Nullable EntityPlayer leader);

    void setHomePosAndDistance(BlockPos pos, int distance);

    default void setHomePos(BlockPos pos) { setHomePosAndDistance(pos, 0); }

    default void free() { setLeader(null); }

    default boolean hasLeader() { return getLeader() != null; }
}
