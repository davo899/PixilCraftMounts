package com.selfdot.pixilcraftmounts.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow public float headYaw;
    @Shadow public float bodyYaw;

    @Inject(method = "getControlledMovementInput", at = @At("HEAD"), cancellable = true)
    protected void injectGetControlledMovementInput(
        PlayerEntity playerEntity, Vec3d vec3d, CallbackInfoReturnable<Vec3d> cir
    ) { }

    @Inject(method = "tickControlled", at = @At("HEAD"))
    protected void injectTickControlled(PlayerEntity playerEntity, Vec3d vec3d, CallbackInfo ci) { }

    @Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
    protected void injectGetMovementSpeed(CallbackInfoReturnable<Float> cir) { }

}
