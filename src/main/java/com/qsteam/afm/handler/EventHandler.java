package com.qsteam.afm.handler;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onLogStripping(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote ||
                !(event.getItemStack().getItem() instanceof ItemAxe) ||
                !(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockLog)) return;



        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
//        world.setBlockState(event.getPos(), AFMBlocks.STRIPPED_LOGS.get().getDefaultState());

        event.getItemStack().damageItem(1, event.getEntityPlayer());
        event.setCanceled(true);

    }
}
