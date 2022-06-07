package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Stream;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow @Final private List<ServerPlayerEntity> players;

    @WrapWithCondition(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private boolean eplayers$playerLeave(PlayerManager manager, Text text, MessageType type, UUID uuid, ClientConnection connection, ServerPlayerEntity player) {
        return !EliminatePlayers.bannedUuids.contains(player.getUuid());
    }
    @Inject(method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At("HEAD"), cancellable = true)
    private void eplayers$actuallyDontBroadcast(Text message, MessageType type, UUID sender, CallbackInfo ci) {
        if (message instanceof TranslatableText transText) {
            String Key = transText.getKey();
            Optional<Object> texts = Arrays.stream(transText.getArgs()).filter(obj -> obj instanceof Text text).findFirst();
            if (Key.equals("multiplayer.player.left") && texts.isPresent() && ((Text)texts.get()).getString().contains("Mouthpiece")) {
                ci.cancel();
            }
        }
    }

    @ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
    private String[] eplayers$dontGetAllPlayersArgType(String[] original) {
        for(int i = 0; i < this.players.size(); ++i) {
            if(!EliminatePlayers.bannedUuids.contains(this.players.get(i).getGameProfile().getId())) {
                original[i] = this.players.get(i).getGameProfile().getName();
            }
        }
        return original;
    }
}
