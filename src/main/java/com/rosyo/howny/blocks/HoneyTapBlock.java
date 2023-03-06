package com.rosyo.howny.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class HoneyTapBlock extends Block {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE =  makeShape();

    public HoneyTapBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {

        if(!(level.getBlockState(blockPos.north()).getBlock().equals(Blocks.BEEHIVE) || level.getBlockState(blockPos.north()).getBlock() == Blocks.BEE_NEST ||
                level.getBlockState(blockPos.west()).getBlock() == Blocks.BEEHIVE || level.getBlockState(blockPos.west()).getBlock() == Blocks.BEE_NEST ||
                level.getBlockState(blockPos.south()).getBlock() == Blocks.BEEHIVE || level.getBlockState(blockPos.south()).getBlock() == Blocks.BEE_NEST ||
                level.getBlockState(blockPos.east()).getBlock() == Blocks.BEEHIVE || level.getBlockState(blockPos.east()).getBlock() == Blocks.BEE_NEST))
        {
            LOGGER.debug("Trying to break tap.");
            level.destroyBlock(blockPos, false);
        }
        level.destroyBlock(blockPos, false);
        level.getRandomPlayer().sendSystemMessage(Component.literal("Hello!"));
        super.tick(blockState, level, blockPos, randomSource);
    }

    /* @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }*/

    public static VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.4375, -0.1875, 0.875, 0.5625, 0.4375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.3125, 0.875, 0.625, 0.5625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.25, 0.625, 0.625, 0.3125, 0.6875), BooleanOp.OR);

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
        return true;
    }
}
