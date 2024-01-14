package dev.rosyo.howny.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HoneyPuddleBlock extends Block {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    protected static final VoxelShape VOXEL_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public HoneyPuddleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VOXEL_SHAPE;
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default:
                return blockState;
        }
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK:
                return blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default:
                return super.mirror(blockState, mirror);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(NORTH, EAST, WEST, SOUTH);
    }
}
