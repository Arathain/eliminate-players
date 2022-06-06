package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapWithCondition(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private boolean eplayers$dontSpawnEffectParticles(World world, ParticleEffect particleEffect, double a, double b, double c, double d, double e, double f) {
        return !((LivingEntity)(Object) this instanceof PlayerEntity player && EliminatePlayers.bannedUuids.contains(player.getUuid()));
    }
    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void eplayers$canTarget(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof PlayerEntity player &&  EliminatePlayers.bannedUuids.contains(player.getUuid()))
            cir.setReturnValue(false);
    }
}
