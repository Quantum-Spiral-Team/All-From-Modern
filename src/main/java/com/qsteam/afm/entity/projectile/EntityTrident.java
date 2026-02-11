package com.qsteam.afm.entity.projectile;

import com.qsteam.afm.handler.EnchantmentHandler;
import com.qsteam.afm.handler.RegistryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import com.qsteam.afm.handler.SoundHandler;
import com.qsteam.afm.api.mixin.ILightningCaster;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

public class EntityTrident extends EntityAbstractArrow {

    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class,
            DataSerializers.BYTE);
    private static final DataParameter<Boolean> ENCHANTED = EntityDataManager.createKey(EntityTrident.class,
            DataSerializers.BOOLEAN);
    public int returningTicks;
    private ItemStack thrownStack = new ItemStack(RegistryHandler.TRIDENT);
    private boolean dealtDamage;

    public EntityTrident(World worldIn) {
        super(worldIn);
    }

    public EntityTrident(World worldIn, EntityLivingBase shooter, ItemStack stack) {
        super(worldIn, shooter);
        this.thrownStack = stack.copy();
        this.dataManager.set(LOYALTY_LEVEL,
                (byte) EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.LOYALTY, stack));
        this.dataManager.set(ENCHANTED, stack.isItemEnchanted());
    }

    @SideOnly(Side.CLIENT)
    public EntityTrident(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.thrownStack = new ItemStack(RegistryHandler.TRIDENT);
    }

    public static DamageSource causeTridentDamage(EntityTrident trident, @Nullable Entity shooter) {
        return new EntityDamageSourceIndirect("trident", trident, shooter == null ? trident : shooter).setProjectile();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOYALTY_LEVEL, (byte) 0);
        this.dataManager.register(ENCHANTED, false);
    }

    public boolean isEnchanted() {
        return this.dataManager.get(ENCHANTED);
    }

    @Override
    public void onUpdate() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity shooter = this.getShooter();
        int loyalty = this.dataManager.get(LOYALTY_LEVEL);
        if (loyalty > 0 && (this.dealtDamage || this.isNoClip()) && shooter != null) {
            if (!this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.getPickupStatus() == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.setDead();
            } else {
                this.setNoClip(true);
                Vec3d relativePos = new Vec3d(shooter.posX - this.posX,
                        shooter.posY + (double) shooter.getEyeHeight() - this.posY, shooter.posZ - this.posZ);
                double speed = 0.05D * loyalty;
                this.setMotion(this.getMotion().scale(0.95D).add(relativePos.normalize().scale(speed)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundHandler.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.onUpdate();
    }

    private boolean shouldReturnToThrower() {
        Entity shooter = this.getShooter();
        if (shooter != null && shooter.isEntityAlive()) {
            return !(shooter instanceof EntityPlayer) || !((EntityPlayer) shooter).isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getArrowStack() {
        ItemStack stack = this.thrownStack.copy();
        return this.thrownStack.copy();
    }

    @Nullable
    @Override
    protected RayTraceResult findEntityOnPath(Vec3d startVec, Vec3d endVec) {
        return this.dealtDamage ? null : super.findEntityOnPath(startVec, endVec);
    }

    @Override
    protected void onHitEntity(RayTraceResult result) {
        Entity target = result.entityHit;
        float damage = 8.0F; // Base trident damage

        if (target instanceof EntityLivingBase) {
            EntityLivingBase livingTarget = (EntityLivingBase) target;
            damage += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingTarget.getCreatureAttribute());
        }

        Entity shooter = this.getShooter();
        DamageSource source = causeTridentDamage(this, shooter == null ? this : shooter);
        this.dealtDamage = true;
        SoundEvent sound = SoundHandler.ITEM_TRIDENT_HIT;

        if (target.attackEntityFrom(source, damage) && target instanceof EntityLivingBase) {
            EntityLivingBase livingTarget = (EntityLivingBase) target;
            if (shooter instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(livingTarget, shooter);
                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) shooter, livingTarget);
            }

            this.arrowHit(livingTarget);
        }

        this.motionX *= -0.01D;
        this.motionY *= -0.1D;
        this.motionZ *= -0.01D;
        float volume = 1.0F;

        if (this.world.isThundering()
                && EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.CHANNELING, this.thrownStack) > 0) {
            BlockPos pos = target.getPosition();
            if (this.world.canSeeSky(pos)) {
                EntityLightningBolt lightningbolt = new EntityLightningBolt(this.world, (double) pos.getX() + 0.5D,
                        pos.getY(), (double) pos.getZ() + 0.5D, false);
                if (shooter instanceof EntityPlayer) {
                    ((ILightningCaster) lightningbolt).setCaster((EntityPlayer) shooter);
                }
                this.world.addWeatherEffect(lightningbolt);
                sound = SoundHandler.ITEM_TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }

        this.playSound(sound, volume, 1.0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Trident", this.thrownStack.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("Trident", 10)) {
            this.thrownStack = new ItemStack(nbt.getCompoundTag("Trident"));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL,
                (byte) EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.LOYALTY, thrownStack));
        this.dataManager.set(ENCHANTED, thrownStack.isItemEnchanted());
    }

    @Override
    protected void tryDespawn() {
        int loyalty = this.dataManager.get(LOYALTY_LEVEL);
        if (this.getPickupStatus() != PickupStatus.ALLOWED || loyalty <= 0) {
            super.tryDespawn();
        }
    }

    @Override
    public float getWaterDrag() {
        return 0.99F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote && (this.inGround || this.isNoClip()) && this.arrowShake <= 0) {
            boolean isOwner = this.shootingEntity == null || this.shootingEntity.equals(entityIn.getPersistentID());

            if (this.isNoClip() && !isOwner)
                return;

            boolean flag = this.getPickupStatus() == PickupStatus.ALLOWED
                    || (this.getPickupStatus() == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode)
                    || this.isNoClip();

            if (flag) {
                ItemStack pickupStack = this.thrownStack.copy();

                if (this.getPickupStatus() == PickupStatus.ALLOWED
                        && !entityIn.inventory.addItemStackToInventory(pickupStack)) {
                    flag = false;
                }

                if (flag) {
                    if (this.isNoClip()) {
                        this.world.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ,
                                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    } else {
                        this.world.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ,
                                SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                                ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }

                    entityIn.onItemPickup(this, 1);
                    this.setDead();
                }
            }
        }
    }

}
