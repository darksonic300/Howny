package dev.rosyo.howny.server.datagen.provider;

import dev.rosyo.howny.Howny;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class HownyBlockStateProvider extends BlockStateProvider {
    public HownyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Howny.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //blockWithItem(BlocksRegistry.HONEY_TAP);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
