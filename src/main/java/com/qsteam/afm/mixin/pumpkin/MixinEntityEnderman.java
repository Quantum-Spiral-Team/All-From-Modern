package com.qsteam.afm.mixin.pumpkin;

import com.qsteam.afm.block.AFMBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityEnderman.class)
public class MixinEntityEnderman {

    @Redirect(
            method = "shouldAttackPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/init/Blocks;PUMPKIN:Lnet/minecraft/block/Block;",
                    opcode = 178 // GETSTATIC
            )
    )
    private Block redirectEndermanPumpkinCheck() {
        return AFMBlocks.CARVED_PUMPKIN;
    }
}