package dev.rosyo.howny.server.datagen.loottable;

import dev.rosyo.howny.common.registry.BlockRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class HownyBlockLootTable extends BlockLootSubProvider {
    public HownyBlockLootTable() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.HONEY_BRICKS.get());
        this.dropSelf(BlockRegistry.CHISELED_HONEY_BRICKS.get());
        this.dropSelf(BlockRegistry.HONEY_TAP.get());
        this.dropOther(BlockRegistry.HONEY_CAULDRON.get(), Items.CAULDRON);
        this.dropSelf(BlockRegistry.HONEY_PUDDLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
