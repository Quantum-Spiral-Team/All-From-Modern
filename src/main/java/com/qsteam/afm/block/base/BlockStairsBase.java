package com.qsteam.afm.block.base;

import com.qsteam.afm.api.item.IItemModel;
import com.qsteam.afm.handler.RegistryHandler;
import com.qsteam.afm.proxy.ClientProxy;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.qsteam.afm.AllFromModern.AFM_TAB;

public class BlockStairsBase extends BlockStairs implements IItemModel {

    public BlockStairsBase(String name, IBlockState modelState) {
        super(modelState);
        setRegistryName(name);
        setTranslationKey(name);
        setCreativeTab(AFM_TAB);
        this.useNeighborBrightness = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ClientProxy.registerBlockModel(this);
    }

}