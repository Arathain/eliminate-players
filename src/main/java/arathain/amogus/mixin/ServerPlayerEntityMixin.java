package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable PlayerPublicKey playerPublicKey) {
        super(world, blockPos, f, gameProfile, playerPublicKey);
    }

    @ModifyVariable(method = "onDeath", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;"), index = 3)
    private Text eplayers$modifyDeathMessage(Text value) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            return Text.empty();
        }
        return value;
    }

    @Inject(method = "sendMessage(Lnet/minecraft/text/Text;Z)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$dontSendMessage(Text message, boolean bl, CallbackInfo ci) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            ci.cancel();
        }
    }
    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void eplayers$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(EliminatePlayers.bannedUuids.contains(oldPlayer.getUuid()) || EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            this.getInventory().clone(oldPlayer.getInventory());
            this.experienceLevel = oldPlayer.experienceLevel;
            this.totalExperience = oldPlayer.totalExperience;
            this.experienceProgress = oldPlayer.experienceProgress;
            this.setScore(oldPlayer.getScore());
        }
    }
}
