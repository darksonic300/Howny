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

/**
 *  A variation of the LayeredCauldronBlock class, that does not handle precipitation
 *  and is only used for honey fluid management.
 */

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

    protected double getContentHeight(BlockState blockState) {
        return (6.0D + (double)blockState.getValue(LEVEL).intValue() * 3.0D) / 16.0D;
    }

    public boolean isFull(BlockState blockState) {
        return blockState.getValue(LEVEL) == 3;
    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 14;
    }


    /**
     * Method that based on the current fluid level inside the cauldron, lowers the level of the fluid
     * by replacing the block with another blockstate.
     */
    public static void lowerFillLevel(BlockState blockState, Level level, BlockPos blockPos) {
        int i = blockState.getValue(LEVEL) - 1;
        BlockState blockstate = i == 0 ? Blocks.CAULDRON.defaultBlockState() : blockState.setValue(LEVEL, Integer.valueOf(i));
        level.setBlockAndUpdate(blockPos, blockstate);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockstate));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153549_) {
        p_153549_.add(LEVEL);
    }
}
