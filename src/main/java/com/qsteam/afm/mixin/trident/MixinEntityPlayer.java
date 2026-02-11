package com.qsteam.afm.mixin.trident;

import com.qsteam.afm.entity.projectile.EntityAbstractArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    private MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onItemPickup(Entity entity, int quantity) {
        if (this.world.isRemote || entity.isDead) return;

        if (entity instanceof EntityAbstractArrow) {
            EntityTracker entitytracker = ((WorldServer)this.world).getEntityTracker();
            entitytracker.sendToTrackingAndSelf(entity, new SPacketCollectItem(entity.getEntityId(), this.getEntityId(), quantity));
        } else super.onItemPickup(entity, quantity);
    }

}
