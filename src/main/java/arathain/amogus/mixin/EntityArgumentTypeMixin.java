package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(EntityArgumentType.class)
public class EntityArgumentTypeMixin {
    @ModifyReturnValue(method = "getPlayers", at = @At("RETURN"))
    private static Collection<ServerPlayerEntity> eplayers$dontGetAllPlayersArgType(Collection<ServerPlayerEntity> original) {
        original.removeIf((serverPlayerEntity -> EliminatePlayers.bannedUuids.contains(serverPlayerEntity.getUuid())));
        return original;
    }
}
