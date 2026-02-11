package com.qsteam.afm.client.model;

import com.qsteam.afm.Tags;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelTrident extends ModelBase {

    public static final ResourceLocation TRIDENT_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/entity/trident.png");

    private final ModelRenderer root;

    public ModelTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.root = new ModelRenderer(this, 0, 0);
        this.root.addBox(-0.5F, -4.0F, -0.5F, 1, 31, 1, 0.0F);

        ModelRenderer basePlate = new ModelRenderer(this, 4, 0);
        basePlate.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1);
        this.root.addChild(basePlate);

        ModelRenderer leftProng = new ModelRenderer(this, 4, 3);
        leftProng.addBox(-2.5F, -3.0F, -0.5F, 1, 4, 1);
        this.root.addChild(leftProng);

        ModelRenderer rightProng = new ModelRenderer(this, 4, 3);
        rightProng.mirror = true;
        rightProng.addBox(1.5F, -3.0F, -0.5F, 1, 4, 1);
        this.root.addChild(rightProng);
    }

    public  void render() {
        this.root.render(0.0625F);
    }

}
