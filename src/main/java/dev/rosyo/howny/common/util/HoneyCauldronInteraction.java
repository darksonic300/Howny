package dev.rosyo.howny.common.util;

import dev.rosyo.howny.common.block.HoneyCauldronBlock;
import dev.rosyo.howny.common.registry.BlockRegistry;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public interface HoneyCauldronInteraction {
        Map<Item, CauldronInteraction> HONEY = newInteractionMap();

        net.minecraft.core.cauldron.CauldronInteraction FILL_HONEY = (blockState, level, blockPos, player, hand, itemStack)
                -> emptyBottle(BlockRegistry.HONEY_CAULDRON.get().defaultBlockState().setValue(HoneyCauldronBlock.LEVEL, Integer.valueOf(3)), level, blockPos, player, hand, itemStack);


        static Object2ObjectOpenHashMap<Item, CauldronInteraction> newInteractionMap() {
            return Util.make(new Object2ObjectOpenHashMap<>(), (p_175646_) -> {
                p_175646_.defaultReturnValue((p_175739_, p_175740_, p_175741_, p_175742_, p_175743_, p_175744_) -> InteractionResult.PASS);
            });
        }
        static void bootStrap() {

            addDefaultInteractions(CauldronInteraction.EMPTY);

            CauldronInteraction.EMPTY.put(Items.HONEY_BOTTLE, (blockState, level, pos, player, hand, itemStack) -> {
                if (itemStack.getItem() != Items.HONEY_BOTTLE) {
                    return InteractionResult.PASS;
                } else {
                    if (!level.isClientSide) {
                        Item item = itemStack.getItem();
                        player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(item));
                        level.setBlockAndUpdate(pos, BlockRegistry.HONEY_CAULDRON.get().defaultBlockState());
                        level.playSound((Player)null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent((Entity)null, GameEvent.FLUID_PLACE, pos);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            });

            addDefaultInteractions(HONEY);

            HONEY.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, hand, itemStack) -> {
                return fillBottle(blockState, level, blockPos, player, hand, itemStack, new ItemStack(Items.HONEY_BOTTLE), SoundEvents.BOTTLE_FILL);
            });

            HONEY.put(Items.HONEY_BOTTLE, (blockState, level, blockPos, player, hand, itemStack) -> {
                return emptyBottle(blockState, level, blockPos, player, hand, itemStack);
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
            HoneyCauldronBlock.lowerFillLevel(state, level, blockPos);
            level.playSound((Player)null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    static InteractionResult emptyBottle(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, ItemStack itemStack) {
        if (blockState.getValue(HoneyCauldronBlock.LEVEL) != 3 && itemStack.getItem() == Items.HONEY_BOTTLE) {
            if (!level.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                level.setBlockAndUpdate(blockPos, blockState.cycle(HoneyCauldronBlock.LEVEL));
                level.playSound((Player)null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity)null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}