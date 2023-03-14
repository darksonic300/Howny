package dev.rosyo.howny.common.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;

public class StingEnchantment extends Enchantment {

    //TODO: Enchantment can be applied to every item

    private static final float CHANCE_PER_LEVEL = 0.15F;

    public StingEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }

    /**
     *  Method responsible for applying the Poison effect to the target after the attack.
     */
    @Override
    public void doPostAttack(LivingEntity livingEntity, Entity entity, int k) {
        if (entity instanceof LivingEntity livingentity) {
            int i = k * 40;
            livingentity.addEffect(new MobEffectInstance(MobEffects.POISON, i, 2));
        }
    }

    @Override
    public boolean canEnchant(ItemStack p_44689_) {
        return true;
    }

    public int getMinCost(int p_45000_) {
        return 10 + 20 * (p_45000_ - 1);
    }

    public int getMaxCost(int p_45002_) {
        return super.getMinCost(p_45002_) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

}
