package dev.rosyo.howny.server.datagen;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.server.datagen.provider.HownyBlockStateProvider;
import dev.rosyo.howny.server.datagen.provider.HownyItemModelProvider;
import dev.rosyo.howny.server.datagen.provider.HownyLootTableProvider;
import dev.rosyo.howny.server.datagen.provider.HownyRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Howny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new HownyRecipeProvider(packOutput));
        generator.addProvider(true, HownyLootTableProvider.create(packOutput));
        generator.addProvider(true, new HownyBlockStateProvider(packOutput, existingFileHelper));

        //TODO: make new providers
        generator.addProvider(true, new HownyItemModelProvider(packOutput, existingFileHelper));
        //generator.addProvider(event.includeServer(), new HownyWorldGenProvider(packOutput, lookupProvider));
    }
}
