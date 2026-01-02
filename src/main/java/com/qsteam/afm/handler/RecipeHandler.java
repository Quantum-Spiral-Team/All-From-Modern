package com.qsteam.afm.handler;

import com.cleanroommc.groovyscript.compat.vanilla.ShapedCraftingRecipe;
import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RecipeHandler {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();

        ResourceLocation woodenButtonLocation = new ResourceLocation("minecraft", "wooden_button");
        ShapelessOreRecipe woodenButton = new ShapelessOreRecipe(
                woodenButtonLocation,
                Blocks.WOODEN_BUTTON,
                "afmPlanks"
        );
        woodenButton.setRegistryName(woodenButtonLocation);
        registry.register(woodenButton);

        ResourceLocation woodenPressurePlateLocation = new ResourceLocation("minecraft", "wooden_pressure_plate");
        ShapedOreRecipe woodenPressurePlate = new ShapedOreRecipe(
                woodenPressurePlateLocation,
                Blocks.WOODEN_PRESSURE_PLATE,
                "PP",
                'P', "afmPlanks"
        );
        woodenPressurePlate.setRegistryName(woodenPressurePlateLocation);
        registry.register(woodenPressurePlate);

        ResourceLocation woodenTrapdoorLocation = new ResourceLocation("minecraft", "trapdoor");
        ShapedOreRecipe woodenTrapdoor = new ShapedOreRecipe(
                woodenTrapdoorLocation,
                new ItemStack(Blocks.TRAPDOOR, 2),
                "PPP",
                "PPP",
                'P', "afmPlanks"
        );
        woodenTrapdoor.setRegistryName(woodenTrapdoorLocation);
        registry.register(woodenTrapdoor);

        ResourceLocation litPumpkinLocation = new ResourceLocation("minecraft", "lit_pumpkin");
        ShapedOreRecipe litPumpkin = new ShapedOreRecipe(
                litPumpkinLocation,
                Blocks.LIT_PUMPKIN,
                "P",
                "T",
                'P', AFMBlocks.CARVED_PUMPKIN,
                'T', Blocks.TORCH
        );
        litPumpkin.setRegistryName(litPumpkinLocation);
        registry.register(litPumpkin);
    }

}
