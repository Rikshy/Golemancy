package de.shyrik.golemancy.entity.ai;

import de.shyrik.golemancy.entity.golem.EntityModGolem;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;

public class GolemAIGoHome extends EntityAIBase {
    protected EntityModGolem golem;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public GolemAIGoHome(EntityModGolem entity) {
        this.golem = entity;
    }

    @Override
    public boolean shouldExecute() {
        return golem.getHomePosition().toLong() != golem.getPosition().toLong() && !golem.hasLeader();
    }

    @Override
    public void startExecuting() {
        oldWaterCost = golem.getPathPriority(PathNodeType.WATER);
        golem.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        golem.getNavigator().clearPath();
        golem.setPathPriority(PathNodeType.WATER, oldWaterCost);
    }

    public void tick() {
        if (--timeToRecalcPath <= 0) {
            timeToRecalcPath = 10;
            BlockPos home = golem.getHomePosition();
            golem.getNavigator().tryMoveToXYZ(home.getX() + 0.5D, home.getY(), home.getZ() + 0.5D, 1.0D);
        }
    }
}
