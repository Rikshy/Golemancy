package de.shyrik.golemancy.item;

import de.shyrik.golemancy.api.IFollowBaton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemBaton extends Item {

    public ItemBaton(Properties properties) {
        super(properties);
    }

    @CapabilityInject(FollowerHandler.class)
    public static Capability<FollowerHandler> FOLLOWER_CAPABILITY = null;

    public static class FollowerHandler {
        private List<IFollowBaton> followers = new ArrayList<>();

        public boolean hasFollowers() { return !followers.isEmpty(); }

        public void addFollower(IFollowBaton follower, EntityPlayer leader) {
            if (!followers.contains(follower)) {
                followers.add(follower);
                follower.setLeader(leader);
            }
        }

        public void unfollowAll(BlockPos newHome) {
            for (IFollowBaton follower : followers) {
                follower.setHomePos(newHome);
                follower.free();
            }

            followers.clear();
        }

        public void unfollowAll() {
            for (IFollowBaton follower : followers) {
                follower.setHomePosAndDistance(follower.getPosition(), 8);
                follower.free();
            }

            followers.clear();
        }
    }

    public class BatonCapabilityProvider implements ICapabilityProvider {

        private FollowerHandler handler;

        BatonCapabilityProvider() {
            handler = new FollowerHandler();
        }

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable EnumFacing side){
            if (cap == FOLLOWER_CAPABILITY)
                return LazyOptional.of(() -> handler).cast();
            return LazyOptional.empty();
        }
    }

    private static Map<UUID, List<ItemStack>> playerBatons = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof FakePlayer)) {
            List<ItemStack> batons = playerBatons.getOrDefault(event.player.getUniqueID(), new ArrayList<>());

            List<ItemStack> toRem = new ArrayList<>();
            for (ItemStack baton : batons) {
                if(!event.player.inventory.hasItemStack(baton)) {
                    toRem.add(baton);
                    baton.getCapability(FOLLOWER_CAPABILITY).ifPresent(FollowerHandler::unfollowAll);
                }
            }
            batons.removeAll(toRem);

            event.player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemBaton && !batons.contains(stack))
                        batons.add(stack);
                }
            });

            playerBatons.putIfAbsent(event.player.getUniqueID(), batons);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (!(playerIn instanceof FakePlayer)) {
            playerIn.swingArm(handIn);
            if (!worldIn.isRemote) {
                double reachDistance = playerIn.getAttribute(EntityPlayer.REACH_DISTANCE).getValue();
                Vec3d startVec = playerIn.getEyePosition(0);
                Vec3d lookVec = playerIn.getLook(0);

                Vec3d endVec = startVec.add(lookVec.x * reachDistance, lookVec.y * reachDistance, lookVec.z * reachDistance);

                RayTraceResult trace = worldIn.rayTraceBlocks(startVec, endVec, RayTraceFluidMode.ALWAYS, false, false);
                if (trace != null && trace.type == RayTraceResult.Type.BLOCK) {
                    playerIn.getHeldItem(handIn).getCapability(FOLLOWER_CAPABILITY).ifPresent(handler -> handler.unfollowAll(trace.getBlockPos()));
                }
            } else {
                playerIn.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.8F, 0.5F + worldIn.rand.nextFloat() * 0.6F);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new BatonCapabilityProvider();
    }
}
