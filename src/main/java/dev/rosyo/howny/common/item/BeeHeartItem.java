package dev.rosyo.howny.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SimpleFoiledItem;

public class BeeHeartItem extends SimpleFoiledItem {

    /*
     *  Bee Heart Item
     *  Simple enchanted item used in the Sting Enchantment crafting.
     */

    public BeeHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
