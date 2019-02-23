package de.shyrik.golemancy.entity.golem;

import de.shyrik.golemancy.Golemancy;
import de.shyrik.golemancy.init.ModEntities;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityModGolem extends EntityGolem {

    public static final ResourceLocation GOLEM = new ResourceLocation(Golemancy.MODID, "golem");

    public EntityModGolem(World world) {
        super(ModEntities.GOLEM, world);
        this.height /= 2F;
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    @Override
    protected void initEntityAI() {
        int i = 0;
        tasks.addTask(i++, new EntityAISwimming(this));
        tasks.addTask(i++, new EntityAIAvoidEntity<>(this, EntityMob.class, 7.0F, 0.5D, 0.5D));
        tasks.addTask(i++, new EntityAIWanderAvoidWater(this, 0.5D));
        tasks.addTask(i++, new EntityAIWatchClosest(this, EntityPlayer.class, 7.0F));
        tasks.addTask(i, new EntityAILookIdle(this));
    }
}
