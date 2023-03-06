package com.rosyo.howny.datagen;

import com.rosyo.howny.Howny;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Howny.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        //generator.addProvider(true, new HownyItemModelProvider(packOutput, existingFileHelper));
        //generator.addProvider(event.includeServer(), new HownyWorldGenProvider(packOutput, lookupProvider));
    }
}
