package dev.rosyo.howny.common.imbuement;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class Imbuement {
    private final EquipmentSlot[] slots;
    private final Imbuement.Type type;
    @Nullable
    protected String descriptionId;


    protected Imbuement(Imbuement.Type type, EquipmentSlot[] equipmentSlots) {
        this.type = type;
        this.slots = equipmentSlots;
    }

    public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity livingEntity) {
        Map<EquipmentSlot, ItemStack> map = Maps.newEnumMap(EquipmentSlot.class);

        for(EquipmentSlot equipmentslot : this.slots) {
            ItemStack itemstack = livingEntity.getItemBySlot(equipmentslot);
            if (!itemstack.isEmpty()) {
                map.put(equipmentslot, itemstack);
            }
        }

        return map;
    }

    public Imbuement.Type getType() {
        return this.type;
    }

    public int getCost(int cost) {
        return 1 + cost * 10;
    }

    @Deprecated // Forge: Use ItemStack aware version in IForgeEnchantment
    public float getDamageBonus(int p_44682_, MobType p_44683_) {
        return 0.0F;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("imbuement", new ResourceLocation("imbuement"));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullname(int i) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());

        mutablecomponent.withStyle(ChatFormatting.YELLOW);

        if (i != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("imbuement.type." + i));
        }

        return mutablecomponent;
    }

    public boolean canImbue(ItemStack itemStack) {
        return itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof PickaxeItem
                || itemStack.getItem() instanceof AxeItem || itemStack.getItem() instanceof ShovelItem
                || itemStack.getItem() instanceof HoeItem;
    }

    public void doPostAttack(LivingEntity livingEntity, Entity entity, int i) {
    }

    public void doPostHurt(LivingEntity livingEntity, Entity entity, int i) {
    }

    public static enum Type {
        WHITE(1),
        AMBER(2),
        DARK(3);

        private final int weight;

        private Type(int i) {
            this.weight = i;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
