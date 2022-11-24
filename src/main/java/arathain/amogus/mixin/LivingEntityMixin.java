package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @WrapWithCondition(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private boolean eplayers$dontSpawnEffectParticles(World world, ParticleEffect particleEffect, double a, double b, double c, double d, double e, double f) {
        return !((LivingEntity)(Object) this instanceof PlayerEntity player && EliminatePlayers.bannedUuids.contains(player.getUuid()));
    }
    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void eplayers$canTarget(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof PlayerEntity player &&  EliminatePlayers.bannedUuids.contains(player.getUuid()))
            cir.setReturnValue(false);
    }
    @Inject(method = "damage", at = @At("HEAD"))
    private void eplayers$fuckOffDiansuTrademark(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(source instanceof EntityDamageSource e && e.getAttacker() instanceof PlayerEntity player && EliminatePlayers.bannedUuids.contains(player.getUuid()) && Registry.ENTITY_TYPE.getId(this.getType()).getPath().contains("soulmould")) {
            NbtCompound nbt = new NbtCompound();
            this.writeCustomDataToNbt(nbt);
            UUID owner = null;
            if(nbt.contains("Owner")) {
                nbt.remove("ActionState");
                nbt.putInt("ActionState", 1);
                owner = nbt.getUuid("Owner");
                nbt.remove("Owner");
            }
            nbt.putUuid("Owner", player.getUuid());
            this.readCustomDataFromNbt(nbt);
            if(this.world.getPlayerByUuid(owner) != null && ((LivingEntity)(Object)this) instanceof HostileEntity h) {
                h.setTarget(this.world.getPlayerByUuid(owner));
            }

        }
    }

    @Override
    public boolean isTeammate(Entity other) {
        if(other instanceof PlayerEntity player &&  EliminatePlayers.bannedUuids.contains(player.getUuid()) && !(((LivingEntity) (Object) this) instanceof PlayerEntity)) {
            return true;
        }
        return super.isTeammate(other);
    }
}
