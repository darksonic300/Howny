package com.rosyo.howny.blocks;

import com.mojang.logging.LogUtils;
import com.rosyo.howny.init.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class HoneyTapBlock extends Block {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 8, 16);
    private static final VoxelShape SHAPE_N = makeShape('n');
    private static final VoxelShape SHAPE_E = makeShape('e');
    private static final VoxelShape SHAPE_S = makeShape('s');
    private static final VoxelShape SHAPE_W = makeShape('w');

    public HoneyTapBlock(Properties properties) {
        super(properties);
    }


    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        Direction direction = blockState.getValue(FACING);
        Block hive = level.getBlockState(blockPos.relative(direction.getOpposite())).getBlock();
        BlockState hiveState = level.getBlockState(blockPos.relative(direction.getOpposite()));
        Block tank = level.getBlockState(blockPos.below()).getBlock();
        BlockState tankState = level.getBlockState(blockPos.below());

        if(hive instanceof BeehiveBlock) {
            if (hiveState.getValue(BeehiveBlock.HONEY_LEVEL) == BeehiveBlock.MAX_HONEY_LEVELS) {
                if (tank instanceof CauldronBlock) {

                    ((BeehiveBlock) hive).resetHoneyLevel(level, hiveState, blockPos.relative(direction.getOpposite()));
                    level.setBlockAndUpdate(blockPos.below(), BlockRegistry.HONEY_CAULDRON.get().defaultBlockState());
                    level.playSound((Player) null, blockPos.below(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos.below());

                } else if (tank instanceof HoneyCauldronBlock && !((HoneyCauldronBlock) tank).isFull(tankState)) {

                    ((BeehiveBlock) hive).resetHoneyLevel(level, hiveState, blockPos.relative(direction.getOpposite()));
                    level.setBlockAndUpdate(blockPos.below(), tankState.cycle(HoneyCauldronBlock.LEVEL));
                    level.playSound((Player) null, blockPos.below(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos.below());
                }
            }
        }
        super.tick(blockState, level, blockPos, randomSource);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape(char direction){
        VoxelShape shape = Shapes.empty();

        switch (direction){
            case 'n':
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 16, 8, 16), BooleanOp.OR);
            case 'e':
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 16, 8, 16), BooleanOp.OR);
            case 's':
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 16, 8, 16), BooleanOp.OR);
            case 'w':
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 16, 8, 16), BooleanOp.OR);
            default:
                shape = Shapes.join(shape, Shapes.box(0, 0, 0, 16, 8, 16), BooleanOp.OR);
        }

        return shape;
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
