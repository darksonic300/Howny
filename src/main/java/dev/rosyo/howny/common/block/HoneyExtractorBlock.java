package dev.rosyo.howny.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public class HoneyExtractorBlock extends RespawnAnchorBlock {

    public static final int MIN_CHARGES = 0;
    public static final int MAX_CHARGES = 4;
    public static final IntegerProperty CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;

    public HoneyExtractorBlock(Properties p_55838_) {
        super(p_55838_);
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, Integer.valueOf(0)));
    }

    public InteractionResult use(BlockState p_55874_, Level p_55875_, BlockPos p_55876_, Player player, InteractionHand p_55878_, BlockHitResult p_55879_) {
        ItemStack itemstack = player.getItemInHand(p_55878_);
        if (p_55878_ == InteractionHand.MAIN_HAND && !itemstack.is(Items.GLASS_BOTTLE)) {
            return InteractionResult.PASS;
        } else if (itemstack.is(Items.GLASS_BOTTLE)){
            itemstack.shrink(1);
            player.addItem(Items.HONEY_BOTTLE.getDefaultInstance());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if(level.getBlockState(blockPos.above()).getBlock().equals(Blocks.BEEHIVE) || level.getBlockState(blockPos.above()).getBlock() == Blocks.BEE_NEST)
        {
            level.getRandomPlayer().sendSystemMessage(Component.literal("Extracting tap!"));
        }
        super.tick(blockState, level, blockPos, randomSource);
    }

    private static boolean canBeCharged(BlockState p_55895_) {
        return p_55895_.getValue(CHARGE) < 4;
    }

    public static boolean canSetSpawn(Level p_55851_) {
        return false;
    }

    public static void charge(Level p_55856_, BlockPos p_55857_, BlockState p_55858_) {
        p_55856_.setBlock(p_55857_, p_55858_.setValue(CHARGE, Integer.valueOf(p_55858_.getValue(CHARGE) + 1)), 3);
        p_55856_.playSound((Player)null, (double)p_55857_.getX() + 0.5D, (double)p_55857_.getY() + 0.5D, (double)p_55857_.getZ() + 0.5D, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void animateTick(BlockState p_221969_, Level p_221970_, BlockPos p_221971_, RandomSource p_221972_) {
        if (p_221969_.getValue(CHARGE) != 0) {
            if (p_221972_.nextInt(100) == 0) {
                p_221970_.playSound((Player)null, (double)p_221971_.getX() + 0.5D, (double)p_221971_.getY() + 0.5D, (double)p_221971_.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            double d0 = (double)p_221971_.getX() + 0.5D + (0.5D - p_221972_.nextDouble());
            double d1 = (double)p_221971_.getY() + 1.0D;
            double d2 = (double)p_221971_.getZ() + 0.5D + (0.5D - p_221972_.nextDouble());
            double d3 = (double)p_221972_.nextFloat() * 0.04D;
            p_221970_.addParticle(ParticleTypes.DRIPPING_HONEY, d0, d1, d2, 0.0D, d3, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55886_) {
        p_55886_.add(CHARGE);
    }

    public boolean hasAnalogOutputSignal(BlockState p_55860_) {
        return true;
    }

    public static int getScaledChargeLevel(BlockState p_55862_, int p_55863_) {
        return Mth.floor((float)(p_55862_.getValue(CHARGE) - 0) / 4.0F * (float)p_55863_);
    }

    public int getAnalogOutputSignal(BlockState p_55870_, Level p_55871_, BlockPos p_55872_) {
        return getScaledChargeLevel(p_55870_, 15);
    }

    public boolean isPathfindable(BlockState p_55865_, BlockGetter p_55866_, BlockPos p_55867_, PathComputationType p_55868_) {
        return false;
    }
}
