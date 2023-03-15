package dev.rosyo.howny;

import com.mojang.logging.LogUtils;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.EnchantmentRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import dev.rosyo.howny.common.registry.VillagerRegistry;
import dev.rosyo.howny.common.tab.HownyTab;
import dev.rosyo.howny.common.util.HoneyCauldronInteraction;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Howny.MOD_ID)
public class Howny {
    public static final String MOD_ID = "howny";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Howny() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BlockRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        EnchantmentRegistry.register(modEventBus);
        VillagerRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(HownyTab::addItemsToTab);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(HoneyCauldronInteraction::bootStrap);
    }
}
