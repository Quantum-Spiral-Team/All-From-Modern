package com.qsteam.afm.potion;

import com.qsteam.afm.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class PotionBase extends Potion {

    private static final ResourceLocation POTION_ATLAS = new ResourceLocation(Tags.MOD_ID, "textures/gui/potions_atlas.png");

    private final int iconIndexX;
    private final int iconIndexY;

    protected PotionBase(String name, boolean isBadEffect, int color, int indexX, int indexY) {
        super(isBadEffect, color);
        this.setPotionName("effect." + name);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID, name));
        this.iconIndexX = indexX;
        this.iconIndexY = indexY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        renderIcon(x + 6, y + 7, mc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        renderIcon(x + 3, y + 3, mc);
    }

    @SideOnly(Side.CLIENT)
    private void renderIcon(int x, int y, Minecraft mc) {
        mc.getTextureManager().bindTexture(POTION_ATLAS);

        int u = this.iconIndexX * 18;
        int v = this.iconIndexY * 18;

        Gui.drawModalRectWithCustomSizedTexture(x, y, (float)u, (float)v, 18, 18, 256, 256);
    }

}
