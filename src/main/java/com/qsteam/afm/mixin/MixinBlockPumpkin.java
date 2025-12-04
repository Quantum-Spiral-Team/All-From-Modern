package com.qsteam.afm.mixin;

import com.qsteam.afm.AllFromModern;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockPumpkin.class)
public class MixinBlockPumpkin {

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockPumpkin;trySpawnGolem(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    private void redirectTrySpawnGolem(BlockPumpkin instance, World worldIn, BlockPos pos) {
        AllFromModern.LOGGER.info("trySpawnGolem call redirected and cancelled");
    }

}