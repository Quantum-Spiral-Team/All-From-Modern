package com.qsteam.afm.mixin.pumpkin;

import com.qsteam.afm.block.AFMBlocks;
import com.qsteam.afm.handler.SoundHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockPumpkin.class)
public abstract class MixinBlockPumpkin extends BlockHorizontal {

    protected MixinBlockPumpkin() {
        super(Material.GOURD, MapColor.ADOBE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote || !playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem().equals(Items.SHEARS)) return false;
        worldIn.setBlockState(pos, AFMBlocks.CARVED_PUMPKIN.getDefaultState().withProperty(FACING, worldIn.getBlockState(pos).getValue(BlockHorizontal.FACING)));
        worldIn.spawnEntity(new EntityItem(worldIn,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                new ItemStack(Items.PUMPKIN_SEEDS, 4)));
        worldIn.playSound(null, pos,
                SoundHandler.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        playerIn.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, playerIn);
        return true;
    }

    @Inject(method = "trySpawnGolem", at = @At("HEAD"), cancellable = true)
    private void cancelGolemSpawn(World world, BlockPos pos, CallbackInfo ci) {
        ci.cancel();
    }

}