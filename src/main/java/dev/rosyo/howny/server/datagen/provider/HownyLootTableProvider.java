package dev.rosyo.howny.server.datagen.provider;

import dev.rosyo.howny.server.datagen.loottable.HownyBlockLootTable;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class HownyLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(HownyBlockLootTable::new, LootContextParamSets.BLOCK)));
    }
}
