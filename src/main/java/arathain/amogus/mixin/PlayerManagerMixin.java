package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @WrapWithCondition(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private boolean eplayer$playerLeave(PlayerManager manager, Text text, MessageType type, UUID uuid, ClientConnection connection, ServerPlayerEntity player) {
        return !EliminatePlayers.bannedUuids.contains(player.getUuid());
    }
}
