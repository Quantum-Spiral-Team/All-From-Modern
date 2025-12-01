package com.qsteam.afm.handler;

import com.qsteam.afm.Tags;
import com.qsteam.afm.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;

public class RecipeHandler {

    public static void registerRecipe(String key) {
        CraftingHelper.register(new ResourceLocation(Tags.MOD_ID, key), (IRecipeFactory) (context, json) -> CraftingHelper.getRecipe(json, context));
    }

    public static void register() {
        registerRecipe(Blocks.BLUE_ICE.getTranslationKey());
    }

}
