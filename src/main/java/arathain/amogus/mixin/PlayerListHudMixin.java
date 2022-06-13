package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;"), index = 6)
    private List<PlayerListEntry> eplayers$modifyDeathMessage(List<PlayerListEntry> list) {
        list.removeIf((entry) -> EliminatePlayers.bannedUuids.contains(entry.getProfile().getId()));
        return list;
    }
}
