package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    @ModifyReturnValue(method = "getPlayers", at = @At("RETURN"))
    private List<ServerPlayerEntity> eplayers$dontGetAllPlayers(List<ServerPlayerEntity> original) {
        original.removeIf((serverPlayerEntity -> EliminatePlayers.bannedUuids.contains(serverPlayerEntity.getUuid())));
        return original;
    }
}
