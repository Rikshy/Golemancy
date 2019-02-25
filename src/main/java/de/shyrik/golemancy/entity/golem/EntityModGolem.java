package de.shyrik.golemancy.entity.golem;

import de.shyrik.golemancy.Golemancy;
import de.shyrik.golemancy.api.IFollowBaton;
import de.shyrik.golemancy.entity.ai.GolemAIFollow;
import de.shyrik.golemancy.entity.ai.GolemAIGoHome;
import de.shyrik.golemancy.init.ModEntities;
import de.shyrik.golemancy.item.ItemBaton;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityModGolem extends EntityGolem implements IFollowBaton /*, IEntityOwnable*/ {

    private static final String NBT_OWNER = "owner";
    public static final ResourceLocation GOLEM = new ResourceLocation(Golemancy.MODID, "golem");
    protected static final DataParameter<Optional<UUID>> LEADER = EntityDataManager.createKey(EntityModGolem.class, DataSerializers.OPTIONAL_UNIQUE_ID);

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
    protected void registerData() {
        super.registerData();
        dataManager.register(LEADER, Optional.empty());
    }

    @Override
    protected void initEntityAI() {
        int i = 0;
        tasks.addTask(i++, new GolemAIGoHome(this));
//        tasks.addTask(i++, new EntityAISwimming(this));
//        tasks.addTask(i++, new EntityAIHarvest(this, 6D, 8));
//        tasks.addTask(i++, new EntityAIAvoidEntity<>(this, EntityMob.class, 7.0F, 0.5D, 0.5D));
//        tasks.addTask(i++, new EntityAIWanderAvoidWater(this, 0.5D));
//        tasks.addTask(i++, new EntityAIWatchClosest(this, EntityPlayer.class, 7.0F));
        tasks.addTask(i++, new GolemAIFollow(this, 1.25D, 16F));
        tasks.addTask(i, new EntityAILookIdle(this));
    }

    public boolean canDespawn() {
        return false;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player != null && !(player instanceof FakePlayer)) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof ItemBaton) {
                stack.getCapability(ItemBaton.FOLLOWER_CAPABILITY).ifPresent(handler -> handler.addFollower(this, player));

                player.swingArm(hand);
                if (player.world.isRemote)
                    player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.8F, 0.5F + player.world.rand.nextFloat() * 0.6F);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    public EntityPlayer getLeader() {
        try
        {
            UUID uuid = dataManager.get(LEADER).orElse(null);
            return uuid == null ? null : world.getPlayerEntityByUUID(uuid);
        }
        catch (IllegalArgumentException ignored) {}

        return null;
    }

    public void setLeader(@Nullable EntityPlayer leader) {
        UUID uuid = leader == null ? null : leader.getUniqueID();
        dataManager.set(LEADER, Optional.ofNullable(uuid));
    }

//    public void setOwnerId(UUID uuid) {
//        dataManager.set(OWNER, Optional.ofNullable(uuid));
//    }
//
//
//    public boolean isOwner(EntityLivingBase entityIn) {
//        return entityIn == getOwner();
//    }

//    @Override
//    public void readAdditional(NBTTagCompound compound) {
//        super.readAdditional(compound);
//        if (compound.hasUniqueId(NBT_OWNER)) {
//            setOwnerId(UUID.fromString(compound.getString(NBT_OWNER)));
//        }
//    }
//
//    @Override
//    public void writeAdditional(NBTTagCompound compound) {
//        super.writeAdditional(compound);
//        compound.putString(NBT_OWNER, getOwnerId() == null ? "" : getOwnerId().toString());
//    }
}
