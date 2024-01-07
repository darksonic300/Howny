package dev.rosyo.howny.server.datagen.provider;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HownyBlockStateProvider extends BlockStateProvider {
    public HownyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Howny.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockRegistry.HONEY_BRICKS);
        wood((RotatedPillarBlock) BlockRegistry.CHISELED_HONEY_BRICKS.get(), (RotatedPillarBlock) Blocks.HONEY_BLOCK);
    }

    public String name(Block block) {
        ResourceLocation location = ForgeRegistries.BLOCKS.getKey(block);
        if (location != null) {
            return location.getPath();
        } else {
            throw new IllegalStateException("Unknown block: " + block.toString());
        }
    }

    public ResourceLocation texture(String name) {
        return this.modLoc("block/" + name);
    }
    public ResourceLocation texture(String name, String location) {
        return this.modLoc("block/" + location + name);
    }
    public ResourceLocation texture(String name, String location, String suffix) {
        return this.modLoc("block/" + location + name + suffix);
    }
    public ResourceLocation extend(ResourceLocation location, String suffix) {
        String var10002 = location.getNamespace();
        String var10003 = location.getPath();
        return new ResourceLocation(var10002, var10003 + suffix);
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    public void log(RotatedPillarBlock block) {
        this.axisBlock(block, this.texture(this.name(block)), this.extend(this.texture(this.name(block)), "_top"));
    }

    public void wood(RotatedPillarBlock block, RotatedPillarBlock baseBlock) {
        this.axisBlock(block, this.texture(this.name(baseBlock)), this.texture(this.name(baseBlock)));
    }

}
