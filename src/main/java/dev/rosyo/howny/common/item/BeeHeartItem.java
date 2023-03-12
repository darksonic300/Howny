package dev.rosyo.howny.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SimpleFoiledItem;

public class BeeHeartItem extends SimpleFoiledItem {
    public BeeHeartItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public Rarity getRarity(ItemStack p_41461_) {
        return Rarity.RARE;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
}
