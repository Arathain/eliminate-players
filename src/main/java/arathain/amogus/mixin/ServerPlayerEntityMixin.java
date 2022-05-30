package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @ModifyVariable(method = "onDeath", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;"), index = 3)
    private Text eplayers$modifyDeathMessage(Text value) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            return LiteralText.EMPTY;
        }
        return value;
    }
    @Inject(method = "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$dontSendMessage(Text message, MessageType type, UUID sender, CallbackInfo ci) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            ci.cancel();
        }
    }
}
