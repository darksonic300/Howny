package dev.rosyo.howny;

import com.mojang.logging.LogUtils;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.EnchantmentRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import dev.rosyo.howny.common.registry.VillagerRegistry;
import dev.rosyo.howny.common.util.HoneyCauldronInteraction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Howny.MODID)
public class Howny {
    public static final String MODID = "howny";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Howny() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BlockRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        EnchantmentRegistry.register(modEventBus);
        VillagerRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(HoneyCauldronInteraction::bootStrap);
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) event.accept(BlockRegistry.HONEY_TAP.get());
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) event.accept(ItemRegistry.BEE_HEART.get());
        if (event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) event.accept(ItemRegistry.HONEY_COOKIE.get());
    }
}
