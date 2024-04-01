package com.selfdot.pixilcraftmounts.mixin;

import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.selfdot.pixilcraftmounts.imixin.IPokemonEntityMixin;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PokemonEntity.class)
public abstract class PokemonEntityMixin extends MobEntityMixin implements IPokemonEntityMixin, JumpingMount {

    @Shadow private Pokemon pokemon;

    @Unique
    private boolean isMount = false;
    @Unique
    private float jumpStrength = 0F;

    @Unique
    public void pixilCraftMounts$setIsMount(boolean isMount) {
        this.isMount = isMount;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void injectDamage(CallbackInfoReturnable<Boolean> cir) {
        if (isMount) cir.setReturnValue(false);
    }
    
    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void injectRemove(CallbackInfo cir) {
        if (isMount) cir.cancel();
    }

    @Inject(method = "shouldSave", at = @At("HEAD"), cancellable = true)
    private void injectShouldSave(CallbackInfoReturnable<Boolean> cir) {
        if (isMount) cir.setReturnValue(false);
    }

    @Override
    protected void injectGetControlledMovementInput(
        PlayerEntity playerEntity, Vec3d vec3d, CallbackInfoReturnable<Vec3d> cir
    ) {
        float f = playerEntity.sidewaysSpeed * 0.5F;
        float g = playerEntity.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        cir.setReturnValue(new Vec3d(f, 0.0, g));
    }

    @Unique
    private boolean isFlyer() {
        return ElementalTypes.INSTANCE.getFLYING().equals(pokemon.getPrimaryType()) ||
            ElementalTypes.INSTANCE.getFLYING().equals(pokemon.getSecondaryType()) ||
            ElementalTypes.INSTANCE.getDRAGON().equals(pokemon.getPrimaryType()) ||
            ElementalTypes.INSTANCE.getDRAGON().equals(pokemon.getSecondaryType());
    }

    @Override
    protected void injectTickControlled(PlayerEntity playerEntity, Vec3d vec3d, CallbackInfo ci) {
        setRotation(playerEntity.getYaw(), playerEntity.getPitch() * 0.5F);
        this.prevYaw = this.bodyYaw = this.headYaw = getYaw();
        if (isLogicalSideForUpdatingMovement() && playerEntity instanceof ClientPlayerEntity clientPlayer) {
            if (isFlyer()) {
                if (clientPlayer.input.jumping) {
                    Vec3d vec3d2 = getVelocity();
                    this.setVelocity(vec3d2.x, clientPlayer.getMountJumpStrength(), vec3d2.z);
                    this.velocityDirty = true;
                }

            } else {
                if (isOnGround() && jumpStrength > 0F) {
                    double d = (double)jumpStrength * (double)getJumpVelocityMultiplier();
                    double e = d + (double)getJumpBoostVelocityModifier();
                    Vec3d vec3d2 = getVelocity();
                    this.setVelocity(vec3d2.x, e, vec3d2.z);
                    this.velocityDirty = true;
                    if (vec3d.z > 0.0) {
                        float g = MathHelper.sin(this.getYaw() * 0.017453292F);
                        float h = MathHelper.cos(this.getYaw() * 0.017453292F);
                        setVelocity(getVelocity().add(-0.4F * g * jumpStrength, 0.0, 0.4F * h * jumpStrength));
                    }
                }

                jumpStrength = 0F;
            }
        }
    }

    @Override
    protected void injectGetControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        if (getFirstPassenger() instanceof LivingEntity livingEntity) {
            cir.setReturnValue(livingEntity);
        }
    }

    @Override
    protected void injectGetMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (getWorld().isClient && isLogicalSideForUpdatingMovement()) {
            cir.setReturnValue((isFlyer() && !isOnGround() ? 40 : 10) / 40f);
        }
    }

    @Override
    protected void injectRemovePassenger(Entity entity, CallbackInfo ci) {
        discard();
    }

    @Override
    protected void injectSaveNbt(NbtCompound nbtCompound, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
    }

    @Override
    public void setJumpStrength(int i) {
        if (i < 0) i = 0;
        jumpStrength = i >= 90 ? 1.0f : 0.4f + 0.4f * (float)i / 90.0f;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void startJumping(int i) { }

    @Override
    public void stopJumping() { }

}
