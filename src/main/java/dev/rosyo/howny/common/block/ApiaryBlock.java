package dev.rosyo.howny.common.block;

import dev.rosyo.howny.common.util.HoneyCauldronInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;


public class ApiaryBlock extends BeehiveBlock {

    //TODO: Inside of this block will be a new dimension with apiary and bee's rooms
    public ApiaryBlock(Properties properties) {
        super(properties);
    }
}
