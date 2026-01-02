package com.qsteam.afm.mixin.pumpkin;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving {

    @Redirect(
            method = "getSlotForItemStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/inventory/EntityEquipmentSlot;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/init/Blocks;PUMPKIN:Lnet/minecraft/block/Block;",
                    opcode = 178
            )
    )
    private static Block redirectPumpkinField() {
        return AFMBlocks.CARVED_PUMPKIN;
    }
}