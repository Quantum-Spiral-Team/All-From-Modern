package com.qsteam.afm.handler;

import com.qsteam.afm.block.AFMBlocks;
import com.qsteam.afm.block.BlockStrippedNewLog;
import com.qsteam.afm.block.BlockStrippedOldLog;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onLogStripping(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        IBlockState state = world.getBlockState(event.getPos());
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof ItemAxe)) return;

        IBlockState newState = null;
        Block block = state.getBlock();

        if (block.equals(Blocks.LOG)) {
            BlockPlanks.EnumType type = state.getValue(BlockOldLog.VARIANT);
            BlockLog.EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);

            newState = AFMBlocks.STRIPPED_LOG.getDefaultState()
                    .withProperty(BlockStrippedOldLog.VARIANT, type)
                    .withProperty(BlockLog.LOG_AXIS, axis);
        }
        else if (block.equals(Blocks.LOG2)) {
            BlockPlanks.EnumType type = state.getValue(BlockNewLog.VARIANT);
            BlockLog.EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);

            newState = AFMBlocks.STRIPPED_LOG2.getDefaultState()
                    .withProperty(BlockStrippedNewLog.VARIANT, type)
                    .withProperty(BlockLog.LOG_AXIS, axis);
        }

        if (newState != null) {
            world.playSound(null, event.getPos(), SoundEvents.BLOCK_WOOD_STEP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            event.getEntityPlayer().swingArm(event.getHand());

            world.setBlockState(event.getPos(), newState, 11);

            stack.damageItem(1, event.getEntityPlayer());
            event.setCanceled(true);
        }
    }

}
