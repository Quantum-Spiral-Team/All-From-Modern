package com.qsteam.afm.handler;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.block.BlockHorizontal.FACING;
import static net.minecraft.init.Blocks.PUMPKIN;

@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onPumpkinShear(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote || event.getItemStack().getItem() != Items.SHEARS) return;
        if (event.getWorld().getBlockState(event.getPos()).getBlock() != PUMPKIN) return;

        EnumFacing facing = event.getFace();
        EnumFacing blockFacing = (facing == EnumFacing.UP || facing == EnumFacing.DOWN) 
            ? event.getEntityPlayer().getHorizontalFacing().getOpposite() 
            : facing;

        event.getWorld().setBlockState(event.getPos(), 
            AFMBlocks.CARVED_PUMPKIN.getDefaultState().withProperty(FACING, blockFacing));
        event.getWorld().spawnEntity(new EntityItem(event.getWorld(), 
            event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, 
            new ItemStack(Items.PUMPKIN_SEEDS, 4)));
        event.getWorld().playSound(null, event.getPos(), 
            SoundsHandler.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        event.getItemStack().damageItem(1, event.getEntityPlayer());
        event.setCanceled(true);
    }
}
