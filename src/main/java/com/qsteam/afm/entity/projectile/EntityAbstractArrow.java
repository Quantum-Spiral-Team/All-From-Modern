package com.qsteam.afm.entity.projectile;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.qsteam.afm.damagesource.AFMDamageSource;
import com.qsteam.afm.mixin.trident.EntityAccessor;
import com.qsteam.afm.util.entity.projectile.AFMProjectileHelper;
import com.qsteam.afm.util.math.AFMMathHelper;
import com.qsteam.afm.util.math.RayTraceContext;
import com.qsteam.afm.util.math.shapes.ISelectionContext;
import com.qsteam.afm.util.math.shapes.VoxelShape;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class EntityAbstractArrow extends Entity implements IProjectile {

    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityAbstractArrow.class,
            DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> SHOOTER = EntityDataManager
            .createKey(EntityAbstractArrow.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Byte> PIERCE_LEVEL = EntityDataManager.createKey(EntityAbstractArrow.class,
            DataSerializers.BYTE);
    private static final DataParameter<Boolean> SHOT_FROM_CROSSBOW = EntityDataManager
            .createKey(EntityAbstractArrow.class, DataSerializers.BOOLEAN);

    @Nullable
    private IBlockState inBlockState;
    protected boolean inGround;
    protected int inGroundTime;
    private PickupStatus pickupStatus;
    public int arrowShake;
    @Nullable
    public UUID shootingEntity;
    private int life;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;
    private SoundEvent hitSound;

    @Nullable
    private IntOpenHashSet piercedEntities;
    @Nullable
    private List<Entity> hitEntities;

    protected EntityAbstractArrow(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.hitSound = this.getDefaultHitGroundSound();
    }

    protected EntityAbstractArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    protected EntityAbstractArrow(World worldIn, EntityLivingBase shooter) {
        this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D,
                shooter.posZ);
        this.setShooter(shooter);
        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = PickupStatus.ALLOWED;
        }
    }

    public void setHitSound(SoundEvent soundIn) {
        this.hitSound = soundIn;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;
        if (Double.isNaN(d)) {
            d = 1.0D;
        }

        d = d * 64.0D * getRenderDistanceWeight();
        return distance < d * d;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(CRITICAL, (byte) 0);
        this.dataManager.register(SHOOTER, Optional.absent());
        this.dataManager.register(PIERCE_LEVEL, (byte) 0);
        this.dataManager.register(SHOT_FROM_CROSSBOW, false);
    }

    public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
        float f1 = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
        this.shoot(f, f1, f2, velocity, inaccuracy);
        this.setMotion(
                this.getMotion().add(shooter.motionX, shooter.onGround ? 0.0D : shooter.motionY, shooter.motionZ));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize()
                .add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                        this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                        this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
                .scale((double) velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(this.horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, (double) f) * (double) (180F / (float) Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.life = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
            int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.life = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        boolean isNoClip = this.noClip;
        Vec3d vec3d = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(vec3d));
            this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState blockstate = this.world.getBlockState(blockpos);

        if (this.arrowShake > 0) {
            this.arrowShake--;
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.inGround && !isNoClip) {
            if (this.inBlockState != blockstate && !this.world.getCollisionBoxes(null, this.getEntityBoundingBox().grow(0.05D)).isEmpty()) {
                this.inGround = false;
                this.life = 0;
                this.ticksInAir = 0;
            } else if (!this.world.isRemote) {
                this.tryDespawn();
            }

            this.inGroundTime++;
        } else {
            this.inGroundTime = 0;
            this.ticksInAir++;
            Vec3d posVec = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d nextPosVec = posVec.add(vec3d);

            RayTraceResult raytraceresult = this.world.rayTraceBlocks(posVec, nextPosVec, false, true, false);

            if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS) {
                nextPosVec = raytraceresult.hitVec;
            }

            while (!this.isDead) {
                RayTraceResult entityraytraceresult = this.findEntityOnPath(posVec, nextPosVec);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.ENTITY) {
                    Entity entity = raytraceresult.entityHit;
                    Entity shooter = this.getShooter();
                    if (entity instanceof EntityPlayer && shooter instanceof EntityPlayer
                            && !((EntityPlayer) shooter).canAttackPlayer((EntityPlayer) entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS
                        && (!isNoClip || raytraceresult.typeOfHit == RayTraceResult.Type.ENTITY)
                        && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.isAirBorne = true;
                }

                if (entityraytraceresult == null || this.getPierceLevel() <= 0 || this.inGround) {
                    break;
                }

                raytraceresult = null;
            }

            vec3d = this.getMotion();
            double d1 = vec3d.x;
            double d2 = vec3d.y;
            double d0 = vec3d.z;
            if (this.getIsCritical()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + d1 * i / 4.0D,
                            this.posY + d2 * i / 4.0D, this.posZ + d0 * i / 4.0D, -d1, -d2 + 0.2D,
                            -d0);
                }
            }

            this.posX += d1;
            this.posY += d2;
            this.posZ += d0;
            float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0);
            float targetYaw = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI));
            float targetPitch = (float) (MathHelper.atan2(d2, f4) * (180D / Math.PI));

            this.prevRotationYaw = AFMProjectileHelper.updateCursor(this.prevRotationYaw, targetYaw);
            this.prevRotationPitch = AFMProjectileHelper.updateCursor(this.prevRotationPitch, targetPitch);

            this.rotationYaw = AFMMathHelper.lerp(this.prevRotationYaw, targetYaw, 0.2F);
            this.rotationPitch = AFMMathHelper.lerp(this.prevRotationPitch, targetPitch, 0.2F);

            float f1 = 0.99F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - d1 * 0.25D,
                            this.posY - d2 * 0.25D, this.posZ - d0 * 0.25D, d1, d2, d0);
                }

                f1 = this.getWaterDrag();
            }

            this.setMotion(vec3d.scale(f1));
            if (!this.hasNoGravity() && !isNoClip) {
                Vec3d vec3d3 = this.getMotion();
                this.setMotion(vec3d3.x, vec3d3.y - 0.05D, vec3d3.z);
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    protected void tryDespawn() {
        this.life++;
        if (this.life >= 1200) {
            this.setDead();
        }
    }

    protected void onHit(RayTraceResult result) {
        RayTraceResult.Type type = result.typeOfHit;
        if (type == RayTraceResult.Type.ENTITY) {
            this.onHitEntity(result);
        } else if (type == RayTraceResult.Type.BLOCK) {
            IBlockState blockState = this.world.getBlockState(result.getBlockPos());
            this.inBlockState = blockState;
            Vec3d vec3d = result.hitVec.subtract(this.posX, this.posY, this.posZ);
            this.setMotion(vec3d);
            Vec3d vec3d1 = vec3d.normalize().scale(0.05D);
            this.posX -= vec3d1.x;
            this.posY -= vec3d1.y;
            this.posZ -= vec3d1.z;
            this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);
            this.setPierceLevel((byte) 0);
            this.setHitSound(SoundEvents.ENTITY_ARROW_HIT);
            this.setShotFromCrossbow(false);
            this.resetPiercingStatus();
            blockState.getBlock().onEntityCollision(this.world, result.getBlockPos(), blockState, this);
        }
    }

    private void resetPiercingStatus() {
        if (this.hitEntities != null) {
            this.hitEntities.clear();
        }
        if (this.piercedEntities != null) {
            this.piercedEntities.clear();
        }
    }

    protected void onHitEntity(RayTraceResult result) {
        Entity entity = result.entityHit;
        float f = (float) this.getMotion().length();
        int damageAmount = MathHelper.ceil(Math.max(f * this.damage, 0.0D));

        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.setDead();
                return;
            }

            this.piercedEntities.add(entity.getEntityId());
        }

        if (this.getIsCritical()) {
            damageAmount += this.rand.nextInt(damageAmount / 2 + 2);
        }

        Entity shooter = this.getShooter();
        DamageSource damageSource;
        if (shooter == null) {
            damageSource = AFMDamageSource.causeAbstractArrowDamage(this, this);
        } else {
            damageSource = AFMDamageSource.causeAbstractArrowDamage(this, shooter);
            if (shooter instanceof EntityLivingBase) {
                ((EntityLivingBase) shooter).setLastAttackedEntity(entity);
            }
        }

        int fireTimer = getFireTimer(entity);
        if (this.isBurning() && !(entity instanceof EntityEnderman)) {
            entity.setFire(5);
        }

        if (entity.attackEntityFrom(damageSource, (float) damageAmount)) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase livingEntity = (EntityLivingBase) entity;
                if (!this.world.isRemote && this.getPierceLevel() <= 0) {
                    livingEntity.setArrowCountInEntity(livingEntity.getArrowCountInEntity() + 1);
                }

                if (this.knockbackStrength > 0) {
                    Vec3d vec3d = new Vec3d(this.getMotion().x, 0.0D, this.getMotion().z)
                            .normalize()
                            .scale(this.knockbackStrength * 0.6D);
                    if (vec3d.lengthSquared() > 0.0D) {
                        livingEntity.addVelocity(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                if (!this.world.isRemote && shooter instanceof EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments(livingEntity, shooter);
                    EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) shooter, livingEntity);
                }

                this.arrowHit(livingEntity);
                if (livingEntity != shooter && livingEntity instanceof EntityPlayer
                        && shooter instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) shooter).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                }

                if (!entity.isEntityAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingEntity);
                }

//                TODO: Implement KILLED_BY_CROSSBOW trigger
//                if (!this.world.isRemote && shooter instanceof EntityPlayerMP) {
//                    EntityPlayerMP serverPlayer = (EntityPlayerMP) shooter;
//                }
            }

            this.playSound(this.hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0 && !(entity instanceof EntityEnderman)) {
                this.setDead();
            }
        } else {
            setFireTimer(entity, fireTimer);
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
                if (this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.setDead();
            }
        }
    }

    private int getFireTimer(Entity entity) {
        return ((EntityAccessor) entity).getFire();
    }

    private void setFireTimer(Entity entity, int timer) {
        ((EntityAccessor) entity).setFire(timer);
    }

    protected SoundEvent getDefaultHitGroundSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }

    protected final SoundEvent getHitGroundSound() {
        return this.hitSound;
    }

    protected abstract void arrowHit(EntityLivingBase living);

    @Nullable
    protected RayTraceResult findEntityOnPath(Vec3d startVec, Vec3d endVec) {
        return AFMProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec,
                this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ)
                        .grow(1.0D),
                entity -> entity instanceof EntityLivingBase &&
                        !entity.isDead &&
                        entity.isEntityAlive() &&
                        entity.canBeCollidedWith() &&
                        (entity != this.getShooter() || this.ticksInAir >= 5) &&
                        (this.piercedEntities == null || !this.piercedEntities.contains(entity.getEntityId())));
    }

    public void setMotion(Vec3d vec3d) {
        this.motionX = vec3d.x;
        this.motionY = vec3d.y;
        this.motionZ = vec3d.z;
    }

    public void setMotion(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public Vec3d getMotion() {
        return new Vec3d(this.motionX, this.motionY, this.motionZ);
    }

    public void setShooter(@Nullable Entity shooter) {
        if (shooter != null) {
            this.shootingEntity = shooter.getPersistentID();
            this.dataManager.set(SHOOTER, Optional.of(this.shootingEntity));
            if (shooter instanceof EntityPlayer) {
                this.pickupStatus = ((EntityPlayer) shooter).capabilities.isCreativeMode ? PickupStatus.CREATIVE_ONLY
                        : PickupStatus.ALLOWED;
            }
        } else {
            this.shootingEntity = null;
            this.dataManager.set(SHOOTER, Optional.absent());
        }
    }

    @Nullable
    public Entity getShooter() {
        if (this.shootingEntity != null && !this.world.isRemote) {
            return ((WorldServer) this.world).getEntityFromUuid(this.shootingEntity);
        } else if (this.world.isRemote) {
            Optional<UUID> optional = this.dataManager.get(SHOOTER);
            if (optional.isPresent()) {
                return this.world.getPlayerEntityByUUID(optional.get());
            }
        }
        return null;
    }

    private double horizontalMag(Vec3d vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("life", (short) this.life);

        if (this.inBlockState != null) {
            compound.setTag("inBlockState", NBTUtil.writeBlockState(new NBTTagCompound(), this.inBlockState));
        }

        compound.setByte("shake", (byte) this.arrowShake);
        compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        compound.setByte("pickup", (byte) this.pickupStatus.ordinal());
        compound.setDouble("damage", this.damage);
        compound.setBoolean("crit", this.getIsCritical());
        compound.setByte("PierceLevel", this.getPierceLevel());
        if (this.shootingEntity != null) {
            compound.setUniqueId("OwnerUUID", this.shootingEntity);
        }
        compound.setString("SoundEvent", SoundEvent.REGISTRY.getNameForObject(this.hitSound).toString());
        compound.setBoolean("ShotFromCrossbow", this.isShotFromCrossbow());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.life = compound.getShort("life");

        if (compound.hasKey("inBlockState", 10)) {
            this.inBlockState = NBTUtil.readBlockState(compound.getCompoundTag("inBlockState"));
        }

        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getBoolean("inGround");
        if (compound.hasKey("damage", 99)) {
            this.damage = compound.getDouble("damage");
        }

        if (compound.hasKey("pickup", 99)) {
            this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
        } else if (compound.hasKey("player", 99)) {
            this.pickupStatus = compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED;
        }

        this.setIsCritical(compound.getBoolean("crit"));
        this.setPierceLevel(compound.getByte("PierceLevel"));

        if (compound.hasUniqueId("OwnerUUID")) {
            this.shootingEntity = compound.getUniqueId("OwnerUUID");
            this.dataManager.set(SHOOTER, Optional.of(this.shootingEntity));
        }

        if (compound.hasKey("SoundEvent", 8)) {
            this.hitSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(compound.getString("SoundEvent")));
        }
        this.setShotFromCrossbow(compound.getBoolean("ShotFromCrossbow"));
    }

    @SuppressWarnings("ConstantConditions")
    public void setEnchantmentEffectsFromEntity(EntityLivingBase shooter, float velocity) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, shooter);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, shooter);
        this.setDamage((double) (velocity * 2.0F) + this.rand.nextGaussian() * 0.25D
                + (double) ((float) this.world.getDifficulty().getId() * 0.11F));
        if (i > 0) {
            this.setDamage(this.getDamage() + i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, shooter) > 0) {
            this.setFire(100);
        }
    }

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    public int getKnockbackStrength() {
        return this.knockbackStrength;
    }

    public boolean getIsCritical() {
        byte b0 = this.dataManager.get(CRITICAL);
        return (b0 & 1) != 0;
    }

    public void setIsCritical(boolean critical) {
        byte b0 = this.dataManager.get(CRITICAL);
        if (critical) {
            this.dataManager.set(CRITICAL, (byte) (b0 | 1));
        } else {
            this.dataManager.set(CRITICAL, (byte) (b0 & -2));
        }
    }

    public void setShotFromCrossbow(boolean fromCrossbow) {
        this.dataManager.set(SHOT_FROM_CROSSBOW, fromCrossbow);
    }

    public boolean isShotFromCrossbow() {
        return this.dataManager.get(SHOT_FROM_CROSSBOW);
    }

    public byte getPierceLevel() {
        return this.dataManager.get(PIERCE_LEVEL);
    }

    public void setPierceLevel(byte level) {
        this.dataManager.set(PIERCE_LEVEL, level);
    }

    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }

    public boolean isNoClip() {
        return this.noClip;
    }

    public float getWaterDrag() {
        return 0.6F;
    }

    public abstract ItemStack getArrowStack();

    public PickupStatus getPickupStatus() {
        return this.pickupStatus;
    }

    public void setPickupStatus(PickupStatus pickupStatus) {
        this.pickupStatus = pickupStatus;
    }

    public enum PickupStatus {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static PickupStatus getByOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                ordinal = 0;
            }
            return values()[ordinal];
        }
    }

}
