package dev.rosyo.howny.server.datagen.provider;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HownyItemModelProvider extends ItemModelProvider {
    public HownyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Howny.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.itemBlock(BlockRegistry.HONEY_BRICKS.get());
        this.itemBlock(BlockRegistry.FLOWERING_LOG_ALTAR.get());
        //this.itemBlock(BlockRegistry.CHISELED_HONEY_BRICKS.get());
        this.simpleItem(ItemRegistry.HONEY_DIPPER);
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Howny.MOD_ID,"block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Howny.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Howny.MOD_ID,"item/" + item.getId().getPath()));
    }

    public String blockName(Block block) {
        ResourceLocation location = ForgeRegistries.BLOCKS.getKey(block);
        if (location != null) {
            return location.getPath();
        } else {
            throw new IllegalStateException("Unknown block: " + block.toString());
        }
    }

    public String itemName(Item item) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(item);
        if (location != null) {
            return location.getPath();
        } else {
            throw new IllegalStateException("Unknown item: " + item.toString());
        }
    }

    protected ResourceLocation texture(String name) {
        return this.modLoc("block/" + name);
    }

    protected ResourceLocation texture(String name, String location) {
        return this.modLoc("block/" + location + name);
    }

    public void item(Item item, String location) {
        ((ItemModelBuilder)this.withExistingParent(this.itemName(item), this.mcLoc("item/generated"))).texture("layer0", this.modLoc("item/" + location + this.itemName(item)));
    }

    public void itemBlock(Block block) {
        this.withExistingParent(this.blockName(block), this.texture(this.blockName(block)));
    }
}
