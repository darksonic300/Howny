package dev.rosyo.howny.common.registry;

import dev.rosyo.howny.Howny;
import dev.rosyo.howny.common.item.BeeHeartItem;
import dev.rosyo.howny.common.item.HoneyDipperItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemRegistry {

    //TODO: Add a BeeBomb Item - Based on the number of collected bees, they will be released on throw and attack the closest entity different from a bee.
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Howny.MOD_ID);

    // Item used for the sting enchantment crafting
    public static final RegistryObject<Item> BEE_HEART = ITEMS.register("bee_heart",
            () -> new BeeHeartItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HONEY_DIPPER = ITEMS.register("honey_dipper",
            () -> new HoneyDipperItem(new Item.Properties().stacksTo(1).durability(100)));

    public static final RegistryObject<Item> HONEY_COOKIE = ITEMS.register("honey_cookie",
            () -> new Item(new Item.Properties().food(FoodRegistry.HONEY_COOKIE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
