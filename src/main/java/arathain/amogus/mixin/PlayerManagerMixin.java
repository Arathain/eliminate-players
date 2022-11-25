package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.component.TranslatableComponent;
import net.minecraft.unmapped.C_tzcijmwg;
import net.minecraft.unmapped.C_zzdolisx;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow @Final private List<ServerPlayerEntity> players;

    @WrapWithCondition(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;method_43514(Lnet/minecraft/text/Text;Z)V"))
    private boolean eplayers$playerLeave(PlayerManager manager, Text text, boolean bl, ClientConnection connect, ServerPlayerEntity player) {
        return !EliminatePlayers.bannedUuids.contains(player.getUuid());
    }
    @Inject(method = "method_43512", at = @At("HEAD"), cancellable = true)
    private void eplayers$actuallyDontBroadcast(Text message, Function<ServerPlayerEntity, Text> playerMessageFactory, boolean bl, CallbackInfo ci) {
        if (message.asComponent() instanceof TranslatableComponent transCon) {
            String Key = transCon.getKey();
            Optional<Object> texts = Arrays.stream(transCon.getArgs()).filter(obj -> obj instanceof Text text).findFirst();
            if (Key.equals("multiplayer.player.left") && texts.isPresent() && ((Text)texts.get()).getString().contains("Mouthpiece")) {
                ci.cancel();
            }
        }
    }
    @Inject(method = "method_44791", at = @At("HEAD"), cancellable = true)
    private void eplayers$noBroadcasty(C_zzdolisx message, Predicate<ServerPlayerEntity> shouldSendFiltered, @Nullable ServerPlayerEntity sender, C_tzcijmwg sourceProfile, MessageType.C_iocvgdxe params, CallbackInfo ci) {
        if(message.method_44125().getString().startsWith("/")) return;
        if(sender == null) return;
        if(EliminatePlayers.bannedUuids.contains(sender.getUuid())) {
            ci.cancel();
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
