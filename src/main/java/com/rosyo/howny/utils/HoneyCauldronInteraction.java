package com.rosyo.howny.utils;

import com.rosyo.howny.blocks.HoneyCauldron;
import com.rosyo.howny.init.BlockRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface HoneyCauldronInteraction {
        Map<Item, CauldronInteraction> HONEY = newInteractionMap();

        net.minecraft.core.cauldron.CauldronInteraction FILL_HONEY = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> emptyBottle(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, BlockRegistry.HONEY_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);


        static Object2ObjectOpenHashMap<Item, CauldronInteraction> newInteractionMap() {
            return Util.make(new Object2ObjectOpenHashMap<>(), (p_175646_) -> {
                p_175646_.defaultReturnValue((p_175739_, p_175740_, p_175741_, p_175742_, p_175743_, p_175744_) -> InteractionResult.PASS);
            });
        }
        static void bootStrap() {

            addDefaultInteractions(HONEY);

            HONEY.put(Items.GLASS_BOTTLE, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> {
                return fillBottle(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(Items.HONEY_BOTTLE), SoundEvents.BOTTLE_FILL);
            });

            CauldronInteraction.EMPTY.put(Items.HONEY_BOTTLE, HoneyCauldronInteraction.FILL_HONEY);

            HONEY.put(Items.HONEY_BOTTLE, (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> {
                return emptyBottle(p_175698_, p_175699_, p_175700_, p_175701_, new ItemStack(Items.GLASS_BOTTLE), p_175697_, SoundEvents.BOTTLE_EMPTY);
            });

        }

        static void addDefaultInteractions(Map<Item, net.minecraft.core.cauldron.CauldronInteraction> interactionMap) {
            interactionMap.put(Items.HONEY_BOTTLE, FILL_HONEY);
        }

    static InteractionResult fillBottle(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, ItemStack itemStack, ItemStack itemStack1, SoundEvent soundEvent) {

        if (!level.isClientSide) {
            Item item = itemStack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.HONEY_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            LayeredCauldronBlock.lowerFillLevel(state, level, blockPos);
            level.playSound((Player)null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    static InteractionResult emptyBottle(Level level, BlockPos blockPos, Player player, InteractionHand hand, ItemStack itemStack, BlockState state, SoundEvent soundEvent) {
        if (state.getValue(LayeredCauldronBlock.LEVEL) != 3 && itemStack.getItem() == Items.HONEY_BOTTLE) {
            if (!level.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                level.setBlockAndUpdate(blockPos, state.cycle(LayeredCauldronBlock.LEVEL));
                level.playSound((Player) null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}