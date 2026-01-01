package com.qsteam.afm.handler;

import com.qsteam.afm.block.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onLogStripping(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof ItemAxe)) return;

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());
        Block block = state.getBlock();
        IBlockState newState = null;

        if (block.equals(Blocks.LOG)) {
            BlockPlanks.EnumType type = state.getValue(BlockOldLog.VARIANT);
            BlockLog.EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);
            newState = AFMBlocks.STRIPPED_LOG.getDefaultState()
                    .withProperty(BlockStrippedOldLog.VARIANT, type)
                    .withProperty(BlockLog.LOG_AXIS, axis);
        } else if (block.equals(Blocks.LOG2)) {
            BlockPlanks.EnumType type = state.getValue(BlockNewLog.VARIANT);
            BlockLog.EnumAxis axis = state.getValue(BlockLog.LOG_AXIS);
            newState = AFMBlocks.STRIPPED_LOG2.getDefaultState()
                    .withProperty(BlockStrippedNewLog.VARIANT, type)
                    .withProperty(BlockLog.LOG_AXIS, axis);
        } else if (block.equals(AFMBlocks.WOOD)) {
            BlockPlanks.EnumType type = state.getValue(BlockOldWood.VARIANT);
            newState = AFMBlocks.STRIPPED_WOOD.getDefaultState()
                    .withProperty(BlockStrippedNewLog.VARIANT, type);
        } else if (block.equals(AFMBlocks.WOOD2)) {
            BlockPlanks.EnumType type = state.getValue(BlockNewWood.VARIANT);
            newState = AFMBlocks.STRIPPED_WOOD2.getDefaultState()
                    .withProperty(BlockStrippedNewLog.VARIANT, type);
        }

        if (newState != null) {
            player.swingArm(event.getHand());

            if (!world.isRemote) {
                WorldServer ws = (WorldServer) world;
                BlockPos pos = event.getPos();

                ws.spawnParticle(EnumParticleTypes.BLOCK_DUST,
                        pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5,
                        6, 0.2, 0.2, 0.2, 0.0,
                        Block.getStateId(state));

                world.playSound(null, pos, SoundHandler.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

                world.setBlockState(pos, newState, 11);
                stack.damageItem(1, player);
            }

            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

}
