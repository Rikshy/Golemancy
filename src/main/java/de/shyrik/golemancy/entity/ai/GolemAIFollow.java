package de.shyrik.golemancy.entity.ai;

import de.shyrik.golemancy.entity.golem.EntityModGolem;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;

public class GolemAIFollow extends EntityAIBase {

    protected EntityModGolem golem;
    private float oldWaterCost;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final double speedModifier;

    public GolemAIFollow(EntityModGolem entity, double speedMod, float stopDist) {
        golem = entity;
        stopDistance = stopDist;
        speedModifier = speedMod;
    }

    @Override
    public boolean shouldExecute() {
        return golem.hasLeader();
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
        EntityPlayer leader = golem.getLeader();
        if (leader != null) {
            golem.getLookHelper().setLookPositionWithEntity(leader, 10.0F, (float)golem.getVerticalFaceSpeed());
            if (--timeToRecalcPath <= 0) {
                timeToRecalcPath = 10;
                if (golem.getDistance(leader) < stopDistance)
                    golem.getNavigator().tryMoveToEntityLiving(leader, speedModifier);
            }
        }
    }
}
