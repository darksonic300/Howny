package dev.rosyo.howny.common.block;

import dev.rosyo.howny.common.util.HoneyCauldronInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;


public class HoneyCauldronBlock extends AbstractCauldronBlock {

    public static final int MIN_FILL_LEVEL = 1;
    public static final int MAX_FILL_LEVEL = 3;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    private static final int BASE_CONTENT_HEIGHT = 6;
    private static final double HEIGHT_PER_LEVEL = 3.0D;

    public HoneyCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties, HoneyCauldronInteraction.HONEY);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(1)));
    }

    protected double getContentHeight(BlockState p_153528_) {
        return (6.0D + (double)p_153528_.getValue(LEVEL).intValue() * 3.0D) / 16.0D;
    }

    public boolean isFull(BlockState p_153555_) {
        return p_153555_.getValue(LEVEL) == 3;
    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 14;
    }

    public static void lowerFillLevel(BlockState p_153560_, Level p_153561_, BlockPos p_153562_) {
        int i = p_153560_.getValue(LEVEL) - 1;
        BlockState blockstate = i == 0 ? Blocks.CAULDRON.defaultBlockState() : p_153560_.setValue(LEVEL, Integer.valueOf(i));
        p_153561_.setBlockAndUpdate(p_153562_, blockstate);
        p_153561_.gameEvent(GameEvent.BLOCK_CHANGE, p_153562_, GameEvent.Context.of(blockstate));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153549_) {
        p_153549_.add(LEVEL);
    }

    protected void handleEntityOnFireInside(BlockState p_153556_, Level p_153557_, BlockPos p_153558_) {
        lowerFillLevel(p_153556_, p_153557_, p_153558_);
    }

    public void entityInside(BlockState p_153534_, Level p_153535_, BlockPos p_153536_, Entity p_153537_) {
        if (!p_153535_.isClientSide && p_153537_.isOnFire() && this.isEntityInsideContent(p_153534_, p_153536_, p_153537_)) {
            p_153537_.clearFire();
            if (p_153537_.mayInteract(p_153535_, p_153536_)) {
                this.handleEntityOnFireInside(p_153534_, p_153535_, p_153536_);
            }
        }

    }
}
