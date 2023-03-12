package com.rosyo.howny.init;

import com.rosyo.howny.Howny;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodRegistry {
    public static final FoodProperties HONEY_COOKIE = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.1F).build();
}
