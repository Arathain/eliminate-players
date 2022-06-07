package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void eplayers$dropInv(CallbackInfo ci) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            ci.cancel();
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void eplayers$getName(CallbackInfoReturnable<Text> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue(new LiteralText("Mouthpiece"));
        }
    }
    @Inject(method = "getEntityName", at = @At("HEAD"), cancellable = true)
    private void eplayers$getEntityName(CallbackInfoReturnable<String> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue("Mouthpiece");
        }
    }
}
