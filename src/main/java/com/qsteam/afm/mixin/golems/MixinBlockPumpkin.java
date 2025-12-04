package com.qsteam.afm.mixin.golems;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockPumpkin.class)
public class MixinBlockPumpkin {

    @Inject(method = "trySpawnGolem", at = @At("HEAD"), cancellable = true)
    private void cancelGolemSpawn(World world, BlockPos pos, CallbackInfo ci) {
        ci.cancel();
    }

}