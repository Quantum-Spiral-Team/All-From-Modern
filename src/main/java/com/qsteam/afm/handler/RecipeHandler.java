package com.qsteam.afm.handler;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class RecipeHandler {

    private static final IForgeRegistryModifiable<IRecipe> RECIPES = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

    public static void removeRecipes() {
        removeRecipe(new ResourceLocation("minecraft:wooden_button"));
        removeRecipe(new ResourceLocation("minecraft:trapdoor"));
        removeRecipe(new ResourceLocation("minecraft:wooden_pressure_plate"));
    }

    private static void removeRecipe(ResourceLocation recipeName) {
        RECIPES.remove(recipeName);
    }

}
