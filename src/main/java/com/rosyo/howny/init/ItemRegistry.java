package com.rosyo.howny.init;

import com.rosyo.howny.Howny;
import com.rosyo.howny.items.BeeHeartItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Howny.MODID);

    public static final RegistryObject<Item> BEE_HEART = ITEMS.register("bee_heart",
            () -> new BeeHeartItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HONEY_COOKIE = ITEMS.register("honey_cookie",
            () -> new Item(new Item.Properties().food(FoodRegistry.HONEY_COOKIE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
