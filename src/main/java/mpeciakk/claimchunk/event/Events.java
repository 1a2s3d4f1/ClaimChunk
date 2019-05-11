package mpeciakk.claimchunk.event;

import mpeciakk.claimchunk.config.ClaimManager;
import mpeciakk.claimchunk.config.Config;
import mpeciakk.claimchunk.models.ClaimData;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.ActionResult;

public class Events {

    public static void init() {
        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos, direction) -> {
            if (world.isClient) return ActionResult.PASS;

            if (Config.PREVENT_BREAKING_BLOCKS) {
                ClaimData cd = ClaimManager.get(blockPos.getX() >> 4, blockPos.getZ() >> 4, playerEntity.dimension.getRawId());

                if (!cd.isOwned()) return ActionResult.PASS;

                if (cd.isOwner(playerEntity.getUuid()) || cd.isMember(playerEntity.getUuid())) {
                    return ActionResult.PASS;
                }
            } else {
                return ActionResult.PASS;
            }

            return ActionResult.FAIL;
        });

        AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            if (Config.PREVENT_ATTACKING_ENTITIES) {
                ClaimData cd = ClaimManager.get((int) entity.x >> 4, (int) entity.z >> 4, playerEntity.dimension.getRawId());

                if (!cd.isOwned()) return ActionResult.PASS;

                if (cd.isOwner(playerEntity.getUuid()) || cd.isMember(playerEntity.getUuid())) {
                    return ActionResult.PASS;
                } else {
                    return ActionResult.FAIL;
                }
            } else {
                return ActionResult.PASS;
            }
        });

        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            if (world.isClient) return ActionResult.PASS;

            if (Config.PREVENT_USING_ITEMS) {
                ClaimData cd = ClaimManager.get(playerEntity.getBlockPos().getX() >> 4, playerEntity.getBlockPos().getZ() >> 4, playerEntity.dimension.getRawId());

                if (!cd.isOwned()) return ActionResult.PASS;

                if (cd.isOwner(playerEntity.getUuid()) || cd.isMember(playerEntity.getUuid())) {
                    return ActionResult.PASS;
                } else {
                    return ActionResult.FAIL;
                }
            } else {
                return ActionResult.PASS;
            }
        });
    }
}
