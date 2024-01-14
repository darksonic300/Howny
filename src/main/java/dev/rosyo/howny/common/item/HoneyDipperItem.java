package dev.rosyo.howny.common.item;

import dev.rosyo.howny.common.registry.BlockRegistry;
import dev.rosyo.howny.common.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class HoneyDipperItem extends Item implements Vanishable {
    public HoneyDipperItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if(useOnContext.getPlayer().getItemInHand(InteractionHand.OFF_HAND).is(Items.HONEY_BOTTLE)){
            useOnContext.getLevel().setBlockAndUpdate(useOnContext.getClickedPos().above(), BlockRegistry.HONEY_PUDDLE.get().defaultBlockState());
            useOnContext.getLevel().playSound(useOnContext.getPlayer(), useOnContext.getClickedPos().above(), SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 100;
    }
}
