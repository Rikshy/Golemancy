package de.shyrik.golemancy.init;

import de.shyrik.golemancy.Golemancy;
import de.shyrik.golemancy.item.ItemBaton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        // register a new block here
        event.getRegistry().register(new ItemSpawnEgg(ModEntities.GOLEM, 40000, 70000, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(new ResourceLocation(Golemancy.MODID, "egg")));
        event.getRegistry().register(new ItemBaton(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)).setRegistryName(new ResourceLocation(Golemancy.MODID, "baton")));
    }
}
