package arathain.amogus.mixin;

import arathain.amogus.EliminatePlayers;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.dedicated.gui.PlayerListGui;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable PlayerPublicKey playerPublicKey) {
        super(world, blockPos, f, gameProfile, playerPublicKey);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid()) && (Math.abs(MinecraftClient.getInstance().getCameraEntity().getRotationVecClient().dotProduct(this.getPos().subtract(cameraX, cameraY, cameraZ).normalize())) > 0.5 || this.isInSneakingPose() || MinecraftClient.getInstance().options.getPerspective().isFrontView())) {
            return false;
        }
        return super.shouldRender(cameraX, cameraY, cameraZ);
    }

    @Override
    public boolean shouldRenderName() {
        return !EliminatePlayers.bannedUuids.contains(this.getUuid());
    }

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void getEliminatedSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue(new Identifier("eplayer", "textures/entity/removed.png"));
        }
    }
    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void getEliminatedSlimModel(CallbackInfoReturnable<String> cir) {
        if(EliminatePlayers.bannedUuids.contains(this.getUuid())) {
            cir.setReturnValue("slim");
        }
    }
}
