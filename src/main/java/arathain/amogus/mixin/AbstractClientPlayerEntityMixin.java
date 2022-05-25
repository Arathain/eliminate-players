package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Override
    public boolean shouldRenderName() {
        return !EliminatePlayers.bannedUuids.contains(this.getUuid());
    }

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void getDriderSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue(new Identifier("eplayer", "textures/entity/removed.png"));
        }
    }
    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void getDriderSlimModel(CallbackInfoReturnable<String> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue("slim");
        }
    }
}
