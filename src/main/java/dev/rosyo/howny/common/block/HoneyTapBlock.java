package dev.rosyo.howny.common.block;

import dev.rosyo.howny.common.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyTapBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HoneyTapBlock(Properties properties) {
        super(properties);
    }

    /* LOGIC HONEY FILL */

    //Fill cauldron with honey
    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        BlockPos posBlockPlacedOn = blockPos.relative(getOppositeFaceDirection(blockState));
        Block blockPlacedOn = level.getBlockState(posBlockPlacedOn).getBlock();
        Block tank = level.getBlockState(blockPos.below()).getBlock();
        BlockState tankState = level.getBlockState(blockPos.below());

        //Checks if block that tap is placed on is Beehive and whether beehive honey will be stored is cauldron block.
        if (ableToFillWithHoney(tankState, tank, blockPlacedOn)) {
            BlockState blockPlacedOnState = level.getBlockState(posBlockPlacedOn);

            //If tap is able to collect, honey is removed and some ambient details are executed
            level.playSound(null, blockPos.below(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos.below());

            //Tank is filled with honey collected from block
            if (canCollectHoney(blockPlacedOn, posBlockPlacedOn, blockPlacedOnState, level)) {
                if (tank instanceof CauldronBlock) {
                    level.setBlockAndUpdate(blockPos.below(), BlockRegistry.HONEY_CAULDRON.get().defaultBlockState());
                }

                if (tank instanceof HoneyCauldronBlock && !((HoneyCauldronBlock) tank).isFull(tankState)) {
                    level.setBlockAndUpdate(blockPos.below(), tankState.cycle(HoneyCauldronBlock.LEVEL));
                }
            }
        }

        super.tick(blockState, level, blockPos, randomSource);
    }


    //Checks if tap is able to start working
    private boolean ableToFillWithHoney(BlockState tankState, Block tank, Block blockPlacedOn) {
        if (blockPlacedOn instanceof BeehiveBlock || blockPlacedOn instanceof FilledHoneycombBlock) {
            if (tank instanceof CauldronBlock) return true;
            if (tank instanceof HoneyCauldronBlock && !((HoneyCauldronBlock) tank).isFull(tankState)) return true;
        }

        return false;
    }

    //If block that is getting collected is BeehiveBlock or FilledHoneycombBlock, change to unfilled with honey
    private boolean canCollectHoney(Block blockPlacedOn, BlockPos posBlockPlacedOn, BlockState blockPlacedOnState, ServerLevel level) {
        if (blockPlacedOn instanceof BeehiveBlock && blockPlacedOnState.getValue(BeehiveBlock.HONEY_LEVEL) == BeehiveBlock.MAX_HONEY_LEVELS) {
            ((BeehiveBlock) blockPlacedOn).resetHoneyLevel(level, blockPlacedOnState, posBlockPlacedOn);
            return true;
        }

        if (blockPlacedOn instanceof FilledHoneycombBlock) {
            level.setBlockAndUpdate(posBlockPlacedOn, Blocks.HONEYCOMB_BLOCK.defaultBlockState());
            return true;
        }

        return false;
    }


    /* BLOCK PLACEMENT LOGIC */

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbourBlockPos, boolean b) {
        if (!canSurvive(blockState, level, blockPos)) {
            level.destroyBlock(blockPos, true);
        }

        super.neighborChanged(blockState, level, blockPos, block, neighbourBlockPos, b);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canAttach(levelReader, blockPos, getOppositeFaceDirection(blockState));
    }

    public static boolean canAttach(LevelReader levelReader, BlockPos blockPos, Direction direction) {
        BlockPos blockpos = blockPos.relative(direction);
        boolean hasBlockToPlace = levelReader.getBlockState(blockpos).isFaceSturdy(levelReader, blockpos, direction.getOpposite());

        return hasBlockToPlace;
    }


    /* ROTATION LOGIC */

    public Direction getOppositeFaceDirection(BlockState blockState) {
        return blockState.getValue(FACING).getOpposite();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return makeShape(blockState.getValue(FACING));
    }

    public static VoxelShape makeShape(Direction direction) {
        VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);

        switch (direction) {
            case SOUTH:
                return SHAPE.move(0, 0, -0.25);
            case NORTH:
                return SHAPE.move(0, 0, +0.25);
            case EAST:
                return SHAPE.move(-0.25, 0, 0);
            case WEST:
                return SHAPE.move(+0.25, 0, 0);
        }

        return SHAPE;
    }

    /* FACING */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return false;
    }
}
