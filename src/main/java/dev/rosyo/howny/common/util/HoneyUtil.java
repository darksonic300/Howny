package dev.rosyo.howny.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;

public class HoneyUtil {

    public static Item byName(String p_43490_) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(p_43490_));
    }

    public static Item getFood(ItemStack p_43580_) {
        return getFood(p_43580_.getTag());
    }

    public static Item getFood(@Nullable CompoundTag p_43578_) {
        return p_43578_ == null ? Items.GLASS_BOTTLE : HoneyUtils.byName(p_43578_.getString("Food"));
    }
    public static ItemStack setFood(ItemStack p_43550_, Item p_43551_) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(p_43551_);
        if (p_43551_ == Items.GLASS_BOTTLE) {
            p_43550_.removeTagKey("Food");
        } else {
            p_43550_.getOrCreateTag().putString("Food", resourcelocation.toString());
        }

        return p_43550_;
    }
}
