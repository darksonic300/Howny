package com.rosyo.howny;

import com.mojang.logging.LogUtils;
import com.rosyo.howny.init.BlockRegistry;
import com.rosyo.howny.init.EnchantmentRegistry;
import com.rosyo.howny.init.ItemRegistry;
import com.rosyo.howny.utils.HoneyCauldronInteraction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Howny.MODID)
public class Howny
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "howny";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Howny()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(this::dataSetup);

        BlockRegistry.register(modEventBus);
        EnchantmentRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            HoneyCauldronInteraction.bootStrap();
        });
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            event.accept(BlockRegistry.HONEY_TAP.get());

        if (event.getTab() == CreativeModeTabs.INGREDIENTS)
            event.accept(ItemRegistry.BEE_HEART.get());
    }

    /*public void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();

        // Client Data
        generator.addProvider(event.includeClient(), new HownyBlockStateProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new HownyItemModelProvider(packOutput, fileHelper));

        // Server Data
        //generator.addProvider(event.includeServer(), new HownyWorldGenData(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new HownyRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), HownyLootTableProvider.create(packOutput));
    }*/

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            //ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.HONEY_TAP.get(), RenderType.translucent());
        }
    }
}
