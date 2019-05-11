package mpeciakk.claimchunk.mixins;

import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.config.Config;
import mpeciakk.claimchunk.models.ClaimData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
    private void interactBlock(PlayerEntity player, World world, ItemStack itemStack_1, Hand hand_1, BlockHitResult blockHitResult_1, CallbackInfoReturnable<ActionResult> cir) {
        if (!Config.PREVENT_BUILDING) return;

        ClaimData cd = ClaimManager.get(blockHitResult_1.getBlockPos().getX() >> 4, blockHitResult_1.getBlockPos().getZ() >> 4, player.dimension.getRawId());

        if (cd.isOwned() && !cd.isOwner(player.getUuid()) && !cd.isMember(player.getUuid())) {
            cir.cancel();
        }
    }
}
